package com.katsman.alfabattle.task1.service.impl

import com.katsman.alfabattle.task1.dto.FinalPricePosition
import com.katsman.alfabattle.task1.dto.FinalPriceReceipt
import com.katsman.alfabattle.task1.dto.Group
import com.katsman.alfabattle.task1.dto.Item
import com.katsman.alfabattle.task1.dto.ItemCountRule
import com.katsman.alfabattle.task1.dto.ItemPosition
import com.katsman.alfabattle.task1.dto.PromoMatrix
import com.katsman.alfabattle.task1.dto.ShoppingCart
import com.katsman.alfabattle.task1.service.CsvConverter
import com.katsman.alfabattle.task1.service.Task1Service
import org.apache.commons.csv.CSVFormat
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import java.io.InputStreamReader
import java.math.BigDecimal
import java.util.concurrent.ConcurrentHashMap
import javax.annotation.PostConstruct
import kotlin.math.max
import kotlin.math.min

@Service
class Task1ServiceImpl(
    private val csvConverter: CsvConverter
) : Task1Service {

    private val items = ConcurrentHashMap<String, Item>()
    private val groups = ConcurrentHashMap<String, Group>()
    private val discounts = ConcurrentHashMap<Int, Double>()
    private val groupDiscounts = ConcurrentHashMap<GroupShopKey, Double>()
    private val itemCountDiscounts = ConcurrentHashMap<ItemShopKey, MutableList<ItemCountRule>>()

    private data class GroupShopKey(
        val groupId: String,
        val shopId: Int
    )

    private data class ItemShopKey(
        val itemId: String,
        val shopId: Int
    )

    companion object {
        private val LOGGER = LoggerFactory.getLogger(Task1ServiceImpl::class.java)
    }

    @Value("classpath:items.csv")
    lateinit var itemsResource: Resource

    @Value("classpath:groups.csv")
    lateinit var groupsResource: Resource

    @PostConstruct
    fun loadItemsAndGroups() {
        val itemsParser = CSVFormat.DEFAULT.withFirstRecordAsHeader()
            .parse(InputStreamReader(itemsResource.inputStream))
        itemsParser.records.forEach {
            val item = csvConverter.convertCsvRecordToItem(it)
            items[item.id] = item
        }

        val groupsParser = CSVFormat.DEFAULT.withFirstRecordAsHeader()
            .parse(InputStreamReader(groupsResource.inputStream))
        groupsParser.records.forEach {
            val group = csvConverter.convertCsvRecordToGroup(it)
            groups[group.id] = group
        }
    }

    override fun promo(promoMatrix: PromoMatrix) {
        LOGGER.info(promoMatrix.toString())

        promoMatrix.loyaltyCardRules?.forEach { loyaltyCardRule ->
            discounts.compute(loyaltyCardRule.shopId) { _, value ->
                return@compute max(loyaltyCardRule.discount, value ?: 0.0)
            }
        }

        promoMatrix.itemGroupRules?.forEach { itemGroupRule ->
            val key = GroupShopKey(itemGroupRule.groupId, itemGroupRule.shopId)
            groupDiscounts.compute(key) { _, value ->
                return@compute max(itemGroupRule.discount, value ?: 0.0)
            }
        }

        promoMatrix.itemCountRules?.forEach { itemCountRule ->
            val key = ItemShopKey(itemCountRule.itemId, itemCountRule.shopId)
            itemCountDiscounts.compute(key) { _, value ->
                val result = value ?: mutableListOf()
                result.add(itemCountRule)
                return@compute result
            }
        }
    }

    override fun receipt(shoppingCart: ShoppingCart): FinalPriceReceipt {
        var totalSum = BigDecimal.ZERO
        var totalDiscount = BigDecimal.ZERO
        LOGGER.info(shoppingCart.toString())
        val positionsByGroup = mutableMapOf<String, MutableMap<String, ItemPosition>>()

        shoppingCart.positions?.forEach {
            val item = items[it.itemId] ?: throw RuntimeException()
            positionsByGroup.compute(item.groupId) { _, value ->
                val result = value ?: mutableMapOf()
                val itemPosition = result[item.id]

                val finalItemPosition = if (itemPosition == null) it
                else ItemPosition(it.itemId, it.quantity + itemPosition.quantity)
                result[item.id] = finalItemPosition
                return@compute result
            }
        }

        val loyaltyDiscount = if (shoppingCart.loyaltyCard == true) discounts[shoppingCart.shopId] ?: discounts[-1]
        ?: 0.0 else 0.0
        val positionsResult = mutableListOf<FinalPricePosition>()

        positionsByGroup.forEach { (groupId, positions) ->
            val groupDiscount = if (positions.size > 1) groupDiscounts[GroupShopKey(groupId, shoppingCart.shopId)]
                ?: groupDiscounts[GroupShopKey(groupId, -1)] ?: 0.0 else 0.0

            val finalDiscount = max(loyaltyDiscount, groupDiscount)
            val finalPriceMultiplier = 1.0 - finalDiscount

            positionsResult.addAll(positions.map { (_, it) ->
                val item = items[it.itemId] ?: throw RuntimeException()
                val finalItemPrice = item.price.multiply(BigDecimal.valueOf(finalPriceMultiplier))
                val discountSum = item.price.subtract(finalItemPrice)
                    .multiply(BigDecimal.valueOf(it.quantity.toLong()))

                val itemCountRules = itemCountDiscounts[ItemShopKey(item.id, shoppingCart.shopId)]
                    ?: itemCountDiscounts[ItemShopKey(item.id, -1)] ?: emptyList<ItemCountRule>()
                val fullSum = item.price.multiply(BigDecimal.valueOf(it.quantity.toLong()))
                var currentSumWithCountDiscount = fullSum
                var currentDiscountItemPrice = item.price

                itemCountRules.forEach { itemCountRule ->
                    val fullGroupsCount = it.quantity / (itemCountRule.triggerQuantity + itemCountRule.bonusQuantity)
                    val paidQuantity = fullGroupsCount * itemCountRule.triggerQuantity +
                        min(it.quantity % (itemCountRule.triggerQuantity + itemCountRule.bonusQuantity),
                            itemCountRule.triggerQuantity)

                    currentSumWithCountDiscount = currentSumWithCountDiscount.min(
                        item.price.multiply(BigDecimal.valueOf(paidQuantity.toLong()))
                    )

                    currentDiscountItemPrice = currentDiscountItemPrice.min(
                        currentSumWithCountDiscount.divide(BigDecimal.valueOf(it.quantity.toLong()))
                    )
                }

                totalSum = totalSum.add(finalItemPrice.min(currentDiscountItemPrice)
                    .multiply(BigDecimal.valueOf(it.quantity.toLong())))
                totalDiscount = totalDiscount.add(discountSum.max(fullSum.subtract(currentSumWithCountDiscount)))

                FinalPricePosition(
                    id = it.itemId,
                    name = item.name,
                    price = finalItemPrice.min(currentDiscountItemPrice),
                    regularPrice = item.price
                )
            })
        }

        discounts.clear()
        groupDiscounts.clear()
        itemCountDiscounts.clear()

        val result = FinalPriceReceipt(
            discount = totalDiscount,
            positions = positionsResult,
            total = totalSum
        )

        LOGGER.info(result.toString())
        return result
    }
}