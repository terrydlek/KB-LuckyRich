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
import RealEstate from '@/views/realestate/RealEstate.vue';
import CoinDetail from '@/components/account/details/CoinDetail.vue';
import FundDetail from '@/components/account/details/FundDetail.vue';
import StockDetail from '@/components/account/details/StockDetail.vue';

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
    path: '/recommend',
    name: 'test',
    component: test,
  },
  {
    path: '/recommend/steadiness',
    name: 'SteadinessList',
    component: SteadinessList,
  },
  {
    path: '/recommend/conservative',
    name: 'ConservativeList',
    component: ConservativeList,
  },
  {
    path: '/recommend/neutral',
    name: 'NeutralList',
    component: NeutralList,
  },
  {
    path: '/recommend/active',
    name: 'ActiveList',
    component: ActiveList,
  },
  {
    path: '/recommend/aggressive',
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
    path: '/recommend/steadiness/:prodname',
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
    path: '/RealEstate',
    name: 'realestate',
    component: RealEstate,
  },
  {
    path: '/recommend/active/:stockCode',
    name: '/StockDetail',
    component: StockDetail,
  },
];
