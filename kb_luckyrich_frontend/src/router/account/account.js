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
import Detail from '@/components/account/details/Detail.vue';
import NewsDetail from '@/components/account/details/NewsDetail.vue';
import AccountBook from '@/views/account/AccountBook.vue';

export default [
  {
    path: '/luckyrich/lookup',
    name: 'lookup',
    component: AccountTotal,
  },
  {
    path: '/luckyrich/asset',
    name: 'asset',
    component: asset,
  },
  {
    path: '/luckyrich/recommend',
    name: 'test',
    component: test,
  },
  {
    path: '/luckyrich/recommend/steadiness',
    name: 'SteadinessList',
    component: SteadinessList,
  },
  {
    path: '/luckyrich/recommend/conservative',
    name: 'ConservativeList',
    component: ConservativeList,
  },
  {
    path: '/luckyrich/recommend/neutral',
    name: 'NeutralList',
    component: NeutralList,
  },
  {
    path: '/luckyrich/recommend/active',
    name: 'ActiveList',
    component: ActiveList,
  },
  {
    path: '/luckyrich/recommend/aggressive',
    name: 'AggresiveList',
    component: AggresiveList,
  },
  {
    path: '/luckyrich/asset/accountupdate',
    name: 'accountupdate',
    component: AccountUpdate,
  },
  {
    path: '/luckyrich/myasset/MyFinancialDashboard',
    name: 'MyFinancialDashboard',
    component: MyFinancialDashboard,
  },
  {
    path: '/luckyrich/myasset/MyDataAgree',
    name: 'myDataAgree',
    component: MyDataAgree,
  },
  {
    path: '/luckyrich/myasset/MyBank',
    name: 'myBank',
    component: MyBank,
  },
  {
    path: '/luckyrich/recommend/steadiness/:prodname',
    name: 'DepositDetail',
    component: DepositDetail,
  },
  {
    path: '/luckyrich/coin/:id',
    name: 'CoinDetail',
    component: CoinDetail,
  },
  {
    path: '/luckyrich/fund/:encodedUrl',
    name: 'FundDetail',
    component: FundDetail,
  },
  {
    path: '/luckyrich/realestate',
    name: 'realestate',
    component: RealEstate,
  },
  {
    path: '/luckyrich/recommend/active/:stockCode',
    name: 'StockDetail',
    component: StockDetail,
  },
  {
    path: '/luckyrich/recommend/funds/:fundUrl',
    name: 'Detail',
    component: Detail,
  },
  {
    path : '/financenews/:code1/:code2',
    name : 'NewsDetail',
    component : NewsDetail
  },
  {
    path : '/luckyrich/accountBook',
    name : 'accountBook',
    component : AccountBook
  }
];
