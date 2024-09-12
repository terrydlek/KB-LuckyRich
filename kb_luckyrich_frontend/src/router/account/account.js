import asset from '@/components/account/asset.vue';
import recommendation from '@/components/account/recommendation.vue';
import AccountUpdate from '@/views/account/AccountUpdate.vue';
import AccountTotal from '@/views/account/AccountTotal.vue';
import Accountbook from '@/views/account/accountbook.vue';
//
export default [
  {
    path: '/accountbook',
    name: 'accountbook',
    component: Accountbook,
  },
  {
    path: '/lookup',
    name: 'lookup',
    component: AccountTotal,
  },
  {
    path: '/accountUpdate',
    name: 'accountUpdate',
    component: AccountUpdate,
  },
  {
    path: '/asset',
    name: 'asset',
    component: asset,
  },
  {
    path: '/recommendation',
    name: 'recommendation',
    component: recommendation,
  },
];
