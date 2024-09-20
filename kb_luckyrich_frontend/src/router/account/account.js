import asset from '@/views/account/asset.vue';
import AccountUpdate from '@/views/account/AccountUpdate.vue';
import AccountTotal from '@/views/account/AccountTotal.vue';
// import Accountbook from '@/views/account/accountbook.vue';
import test from '@/views/account/test.vue';
import SteadinessList from '@/components/account/recommend/SteadinessList.vue';
import ConservativeList from '@/components/account/recommend/ConservativeList.vue';
import NeutralList from '@/components/account/recommend/NeutralList.vue';
import ActiveList from '@/components/account/recommend/ActiveList.vue';
import AggresiveList from '@/components/account/recommend/AggresiveList.vue';

export default [
  // {
  //   path: '/accountbook',
  //   name: 'accountbook',
  //   component: Accountbook,
  // },
  {
    path: '/lookup',
    name: 'lookup',
    component: AccountTotal,
  },
  {
    path: '/asset',
    name: 'asset',
    component: asset,
  },
  {
    path: '/account/test',
    name: 'test',
    component: test,
  },
  {
    path: '/account/SteadinessList',
    name: 'SteadinessList',
    component: SteadinessList,
  },
  {
    path: '/account/ConservativeList',
    name: 'ConservativeList',
    component: ConservativeList,
  },
  {
    path: '/account/NeutralList',
    name: 'NeutralList',
    component: NeutralList,
  },
  {
    path: '/account/ActiveList',
    name: 'ActiveList',
    component: ActiveList,
  },
  {
    path: '/account/AggresiveList',
    name: 'AggresiveList',
    component: AggresiveList,
  },
  {
    path: '/asset/accountupdate',
    name: 'accountupdate',
    component: AccountUpdate,
  },
];
