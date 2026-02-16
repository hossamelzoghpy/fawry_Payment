import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { MainLayoutComponent } from './layout/main-layout/main-layout.component';
import { GatewayListComponent } from './pages/gateway/gateway-list/gateway-list.component';
import { GatewayAvailabilityListComponent } from './pages/gateway-availability/gateway-availability-list/gateway-availability-list.component';
import { RecommendComponent } from './pages/recommend/recommend.component';
import { ScoringFactorComponent } from './pages/scoring-factor/scoring-factor.component';
import { TransactionsComponent } from './pages/transactions/transactions.component';
import { authGuard } from './guards/auth.guard';
import { adminGuard } from './guards/admin-guard';
import { userGuard } from './guards/user-guard';
export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },


  {
    path: '',
    component: MainLayoutComponent,
    canActivate: [authGuard],
    children: [
  
      {
        path: 'gateways',
        component: GatewayListComponent,
        canActivate: [adminGuard]
      },
      {
        path: 'availability',
        component: GatewayAvailabilityListComponent,
        canActivate: [adminGuard]
      },
      {
        path: 'scoring-factor',
        component: ScoringFactorComponent,
        canActivate: [adminGuard]
      },

      // User routes
      {
        path: 'transactions',
        component: TransactionsComponent,
        canActivate: [userGuard]
      },
      {
        path: 'recommend',
        component: RecommendComponent,
        canActivate: [userGuard]
      }
    ]
  },

  // 404 fallback
  { path: '**', redirectTo: 'login' }
];