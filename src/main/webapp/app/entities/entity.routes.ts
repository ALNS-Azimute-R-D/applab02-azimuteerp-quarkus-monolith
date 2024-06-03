import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'country',
    data: { pageTitle: 'azimuteErpQuarkusAngularMonolith02App.country.home.title' },
    loadChildren: () => import('./country/country.routes'),
  },
  {
    path: 'province',
    data: { pageTitle: 'azimuteErpQuarkusAngularMonolith02App.province.home.title' },
    loadChildren: () => import('./province/province.routes'),
  },
  {
    path: 'town-city',
    data: { pageTitle: 'azimuteErpQuarkusAngularMonolith02App.townCity.home.title' },
    loadChildren: () => import('./town-city/town-city.routes'),
  },
  {
    path: 'district',
    data: { pageTitle: 'azimuteErpQuarkusAngularMonolith02App.district.home.title' },
    loadChildren: () => import('./district/district.routes'),
  },
  {
    path: 'locality',
    data: { pageTitle: 'azimuteErpQuarkusAngularMonolith02App.locality.home.title' },
    loadChildren: () => import('./locality/locality.routes'),
  },
  {
    path: 'tenant',
    data: { pageTitle: 'azimuteErpQuarkusAngularMonolith02App.tenant.home.title' },
    loadChildren: () => import('./tenant/tenant.routes'),
  },
  {
    path: 'type-of-organization',
    data: { pageTitle: 'azimuteErpQuarkusAngularMonolith02App.typeOfOrganization.home.title' },
    loadChildren: () => import('./type-of-organization/type-of-organization.routes'),
  },
  {
    path: 'organization',
    data: { pageTitle: 'azimuteErpQuarkusAngularMonolith02App.organization.home.title' },
    loadChildren: () => import('./organization/organization.routes'),
  },
  {
    path: 'business-unit',
    data: { pageTitle: 'azimuteErpQuarkusAngularMonolith02App.businessUnit.home.title' },
    loadChildren: () => import('./business-unit/business-unit.routes'),
  },
  {
    path: 'organization-domain',
    data: { pageTitle: 'azimuteErpQuarkusAngularMonolith02App.organizationDomain.home.title' },
    loadChildren: () => import('./organization-domain/organization-domain.routes'),
  },
  {
    path: 'organization-attribute',
    data: { pageTitle: 'azimuteErpQuarkusAngularMonolith02App.organizationAttribute.home.title' },
    loadChildren: () => import('./organization-attribute/organization-attribute.routes'),
  },
  {
    path: 'type-of-person',
    data: { pageTitle: 'azimuteErpQuarkusAngularMonolith02App.typeOfPerson.home.title' },
    loadChildren: () => import('./type-of-person/type-of-person.routes'),
  },
  {
    path: 'person',
    data: { pageTitle: 'azimuteErpQuarkusAngularMonolith02App.person.home.title' },
    loadChildren: () => import('./person/person.routes'),
  },
  {
    path: 'organization-role',
    data: { pageTitle: 'azimuteErpQuarkusAngularMonolith02App.organizationRole.home.title' },
    loadChildren: () => import('./organization-role/organization-role.routes'),
  },
  {
    path: 'organization-membership',
    data: { pageTitle: 'azimuteErpQuarkusAngularMonolith02App.organizationMembership.home.title' },
    loadChildren: () => import('./organization-membership/organization-membership.routes'),
  },
  {
    path: 'organization-member-role',
    data: { pageTitle: 'azimuteErpQuarkusAngularMonolith02App.organizationMemberRole.home.title' },
    loadChildren: () => import('./organization-member-role/organization-member-role.routes'),
  },
  {
    path: 'asset-type',
    data: { pageTitle: 'azimuteErpQuarkusAngularMonolith02App.assetType.home.title' },
    loadChildren: () => import('./asset-type/asset-type.routes'),
  },
  {
    path: 'raw-asset-proc-tmp',
    data: { pageTitle: 'azimuteErpQuarkusAngularMonolith02App.rawAssetProcTmp.home.title' },
    loadChildren: () => import('./raw-asset-proc-tmp/raw-asset-proc-tmp.routes'),
  },
  {
    path: 'asset',
    data: { pageTitle: 'azimuteErpQuarkusAngularMonolith02App.asset.home.title' },
    loadChildren: () => import('./asset/asset.routes'),
  },
  {
    path: 'asset-metadata',
    data: { pageTitle: 'azimuteErpQuarkusAngularMonolith02App.assetMetadata.home.title' },
    loadChildren: () => import('./asset-metadata/asset-metadata.routes'),
  },
  {
    path: 'invoice',
    data: { pageTitle: 'azimuteErpQuarkusAngularMonolith02App.invoice.home.title' },
    loadChildren: () => import('./invoice/invoice.routes'),
  },
  {
    path: 'payment-method',
    data: { pageTitle: 'azimuteErpQuarkusAngularMonolith02App.paymentMethod.home.title' },
    loadChildren: () => import('./payment-method/payment-method.routes'),
  },
  {
    path: 'payment',
    data: { pageTitle: 'azimuteErpQuarkusAngularMonolith02App.payment.home.title' },
    loadChildren: () => import('./payment/payment.routes'),
  },
  {
    path: 'warehouse',
    data: { pageTitle: 'azimuteErpQuarkusAngularMonolith02App.warehouse.home.title' },
    loadChildren: () => import('./warehouse/warehouse.routes'),
  },
  {
    path: 'supplier',
    data: { pageTitle: 'azimuteErpQuarkusAngularMonolith02App.supplier.home.title' },
    loadChildren: () => import('./supplier/supplier.routes'),
  },
  {
    path: 'brand',
    data: { pageTitle: 'azimuteErpQuarkusAngularMonolith02App.brand.home.title' },
    loadChildren: () => import('./brand/brand.routes'),
  },
  {
    path: 'product',
    data: { pageTitle: 'azimuteErpQuarkusAngularMonolith02App.product.home.title' },
    loadChildren: () => import('./product/product.routes'),
  },
  {
    path: 'inventory-transaction',
    data: { pageTitle: 'azimuteErpQuarkusAngularMonolith02App.inventoryTransaction.home.title' },
    loadChildren: () => import('./inventory-transaction/inventory-transaction.routes'),
  },
  {
    path: 'stock-level',
    data: { pageTitle: 'azimuteErpQuarkusAngularMonolith02App.stockLevel.home.title' },
    loadChildren: () => import('./stock-level/stock-level.routes'),
  },
  {
    path: 'customer',
    data: { pageTitle: 'azimuteErpQuarkusAngularMonolith02App.customer.home.title' },
    loadChildren: () => import('./customer/customer.routes'),
  },
  {
    path: 'customer-type',
    data: { pageTitle: 'azimuteErpQuarkusAngularMonolith02App.customerType.home.title' },
    loadChildren: () => import('./customer-type/customer-type.routes'),
  },
  {
    path: 'category',
    data: { pageTitle: 'azimuteErpQuarkusAngularMonolith02App.category.home.title' },
    loadChildren: () => import('./category/category.routes'),
  },
  {
    path: 'article',
    data: { pageTitle: 'azimuteErpQuarkusAngularMonolith02App.article.home.title' },
    loadChildren: () => import('./article/article.routes'),
  },
  {
    path: 'order',
    data: { pageTitle: 'azimuteErpQuarkusAngularMonolith02App.order.home.title' },
    loadChildren: () => import('./order/order.routes'),
  },
  {
    path: 'order-item',
    data: { pageTitle: 'azimuteErpQuarkusAngularMonolith02App.orderItem.home.title' },
    loadChildren: () => import('./order-item/order-item.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
