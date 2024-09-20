import asset from '@/views/account/asset.vue';
import AccountUpdate from '@/views/account/AccountUpdate.vue';
import AccountTotal from '@/views/account/AccountTotal.vue';
import test from '@/views/account/test.vue';
import SteadinessList from '@/components/account/recommend/SteadinessList.vue';
import ConservativeList from '@/components/account/recommend/ConservativeList.vue';
import Neutrility from '@/components/account/recommend/Neutrility.vue';
import ActiveList from '@/components/account/recommend/ActiveList.vue';
import AggresiveList from '@/components/account/recommend/AggresiveList.vue';
import MyStock from '@/components/account/accountRegi/MyStock.vue';
import MyEstate from '@/components/account/accountRegi/MyEstate.vue';
import MyCar from '@/components/account/accountRegi/MyCar.vue';
import MyBank from '@/components/account/accountRegi/MyBank.vue';
import MyDataAgree from '@/components/account/accountRegi/MyDataAgree.vue';

export default [
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
    path: '/account/Neutrility',
    name: 'Neutrility',
    component: Neutrility,
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
    component: AccountUpdate
  },
  {
    path: '/myasset/MyStock',
    name: 'myStock',
    component: MyStock
  },
  {
    path: '/myasset/MyEstate',
    name: 'myEstate',
    component: MyEstate
  },
  {
    path: '/myasset/MyCar',
    name: 'myCar',
    component: MyCar
  },
  {
    path: '/myasset/MyBank',
    name: 'myBank',
    component: MyBank
  },
  {
    path: '/myasset/MyDataAgree',
    name: 'myDataAgree',
    component: MyDataAgree
  },
];
