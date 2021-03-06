# 大纲

## **目的**

这个数据库的目的是处理客户的基本购买行为。以Target为例，
模拟了购物的离线操作。该数据库将由批发公司（如Target）
的销售人员、经理和其他员工使用。

## **解决的业务问题**

1. 本数据库可处理离线商店公司的基本人力资源管理。
2. 客户可以在线下商店购买产品。因此，存储量将减少。
3. 客户可在特定促销期间申请折扣。促销活动可能与商店不同。
4. 当产品缺货时，商店将从供应商处采购产品。
5. 商店将根据订单重新计算产品的储存量。

## **业务规则**

1. 每个公司都有一个或多个区域（管理部分）。
2. 每个区域将有一个或多个商店（离线）。
3. 每个商店都有一个或多个产品。
4. 每个商店可能有一个或多个折扣。
5. 每个产品将由一个或多个供应商提供。
6. 每个客户/员工必须是一个人。
7. 每个门店/区域/公司员工必须是一名员工。
8. 每个客户可能有一个或多个订单。
9. 每个订单将有一个或多个产品。
