import accountbook from '@/components/account/accountbook.vue';
import lookup from '@/components/account/lookup.vue';
import accountUpdate from '@/components/account/accountUpdate.vue';
import asset from '@/components/account/asset.vue';
import recommendation from '@/components/account/recommendation.vue';

export default [
  {
    path: '/accountbook',
    name: 'accountbook',
    component: accountbook,
  },
  {
    path: '/lookup',
    name: 'lookup',
    component: lookup,
  },
  {
    path: '/accountUpdate',
    name: 'accountUpdate',
    component: accountUpdate,
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
