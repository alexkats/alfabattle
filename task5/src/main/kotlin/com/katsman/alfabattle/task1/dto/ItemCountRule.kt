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
 * Параметры скидки формата \"N+k\" (N+k единиц товара по цене N единиц)
 * @param bonusQuantity Количество единиц товара в подарок
 * @param itemId ID товара
 * @param shopId Номер магазина, -1 для акции сети
 * @param triggerQuantity Количество единиц для применения скидки
 */
data class ItemCountRule(
    /* Количество единиц товара в подарок */
    val bonusQuantity: Int,
    /* ID товара */
    val itemId: String,
    /* Номер магазина, -1 для акции сети */
    val shopId: Int,
    /* Количество единиц для применения скидки */
    val triggerQuantity: Int
)
