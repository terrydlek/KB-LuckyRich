import asset from '@/components/account/asset.vue';
import AccountUpdate from '@/views/account/AccountUpdate.vue';
import AccountTotal from '@/views/account/AccountTotal.vue';
import Accountbook from '@/views/account/accountbook.vue';
import test from '@/views/test.vue';
import SteadinessList from '@/components/account/recommend/SteadinessList.vue';
import ConservativeList from '@/components/account/recommend/ConservativeList.vue';
import Neutrility from '@/components/account/recommend/Neutrility.vue';
import ActiveList from '@/components/account/recommend/ActiveList.vue';
import AggresiveList from '@/components/account/recommend/AggresiveList.vue';
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
    path: '/test',
    name: 'test',
    component: test,
  },
  {
    path: '/SteadinessList',
    name: 'SteadinessList',
    component: SteadinessList,
  },
  {
    path: '/ConservativeList',
    name: 'ConservativeList',
    component: ConservativeList,
  },
  {
    path: '/Neutrility',
    name: 'Neutrility',
    component: Neutrility,
  },
  {
    path: '/ActiveList',
    name: 'ActiveList',
    component: ActiveList,
  },
  {
    path: '/AggresiveList',
    name: 'AggresiveList',
    component: AggresiveList,
  },
];
