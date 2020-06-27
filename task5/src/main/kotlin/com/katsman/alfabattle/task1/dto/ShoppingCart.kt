/**
 * На промоигле
 * Задача для Alfa Battle от X5 Retail Group
 *
 * OpenAPI spec version: 1.0.0
 *
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */
package com.katsman.alfabattle.task1.dto

/**
 * Данные о магазине и товарах в корзине
 * @param loyaltyCard Признак предоставленния карты лояльности
 * @param positions Позиции с товарами в корзине
 * @param shopId Номер магазина
 */
data class ShoppingCart(
    /* Признак предоставленния карты лояльности */
    val loyaltyCard: Boolean?,
    /* Позиции с товарами в корзине */
    val positions: List<ItemPosition>?,
    /* Номер магазина */
    val shopId: Int
)
