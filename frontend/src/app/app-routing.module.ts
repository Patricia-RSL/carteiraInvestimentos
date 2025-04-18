import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PortfolioComponent } from './components/page/portfolio/portfolio.component';
import { HistoricoTransacoesComponent } from './components/page/historico-transacoes/historico-transacoes.component';

const routes: Routes = [
  { path: 'portfolio', component: PortfolioComponent },
  { path: 'historico-transacoes', component: HistoricoTransacoesComponent },
  { path: '', redirectTo: '/portfolio', pathMatch: 'full' }, // Redireciona para /portfolio por padrão
  { path: '**', redirectTo: '/portfolio' } // Qualquer outra rota vai para /portfolio
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
