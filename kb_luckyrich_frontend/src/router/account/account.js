import asset from '@/views/account/asset.vue';
import AccountUpdate from '@/views/account/AccountUpdate.vue';
import AccountTotal from '@/views/account/AccountTotal.vue';
import test from '@/views/account/test.vue';
import SteadinessList from '@/components/account/recommend/SteadinessList.vue';
import ConservativeList from '@/components/account/recommend/ConservativeList.vue';
import NeutralList from '@/components/account/recommend/NeutralList.vue';
import ActiveList from '@/components/account/recommend/ActiveList.vue';
import AggresiveList from '@/components/account/recommend/AggresiveList.vue';
import MyFinancialDashboard from '@/views/account/MyFinancialDashboard.vue';
import MyDataAgree from '@/components/account/accountRegi/MyDataAgree.vue';
import MyBank from '@/components/account/accountRegi/MyBank.vue';
import DepositDetail from '@/components/account/details/DepositDetail.vue';
import AccountBook from '@/components/account/AccountBook.vue';
import RealEstate from '@/views/realestate/RealEstate.vue';
import CoinDetail from '@/components/account/details/CoinDetail.vue';
import FundDetail from '@/components/account/details/FundDetail.vue';

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
  {
    path: '/myasset/MyFinancialDashboard',
    name: 'MyFinancialDashboard',
    component: MyFinancialDashboard,
  },
  {
    path: '/myasset/MyDataAgree',
    name: 'myDataAgree',
    component: MyDataAgree,
  },
  {
    path: '/myasset/MyBank',
    name: 'myBank',
    component: MyBank,
  },
  {
    path: '/deposit/:prodname',
    name: 'DepositDetail',
    component: DepositDetail,
  },
  {
    path: '/coin/:id',
    name: 'CoinDetail',
    component: CoinDetail,
  },
  {
    path: '/fund/:encodedUrl',
    name: 'FundDetail',
    component: FundDetail,
  },
  {
    path: '/account/AccountBook',
    name: 'accountbook',
    component: AccountBook,
  },
  {
    path: '/RealEstate',
    name: 'realestate',
    component: RealEstate,
  },
];
