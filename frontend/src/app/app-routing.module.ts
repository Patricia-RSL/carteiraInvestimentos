import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PortfolioComponent } from './components/page/portfolio/portfolio.component';
import { HistoricoTransacoesComponent } from './components/page/historico-transacoes/historico-transacoes.component';
import { LoginComponent } from './components/page/login/login.component';
import { RegisterComponent } from './components/page/register/register.component';
import { AuthGuard } from './@core/services/auth/auth.guard';


const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'portfolio', component: PortfolioComponent, canActivate: [AuthGuard] }, 
  { path: 'historico-transacoes', component: HistoricoTransacoesComponent, canActivate: [AuthGuard] },
  { path: '', redirectTo: '/portfolio', pathMatch: 'full' },
  { path: '**', redirectTo: '/portfolio' } 
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
