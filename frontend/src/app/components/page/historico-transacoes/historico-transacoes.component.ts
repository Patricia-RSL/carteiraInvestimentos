import { Component } from '@angular/core';
import { OperationType, UserTrade } from 'src/app/@core/BackendModel';
import { HistoricoTransacoesService } from 'src/app/@core/services/historico-transacoes.service';

@Component({
  selector: 'app-historico-transacoes',
  templateUrl: './historico-transacoes.component.html',
  styleUrls: ['./historico-transacoes.component.css']
})
export class HistoricoTransacoesComponent {
  transacoes: UserTrade[] = [];
  filtro: string = '';
  displayedColumns: string[] = ['data', 'simbolo', 'tipo', 'preco', 'quantidade', 'total'];


  constructor(private historicoTransacoesService: HistoricoTransacoesService) {}

  ngOnInit(): void {
    this.carregarTransacoes();
  }

  carregarTransacoes(): void {
    this.historicoTransacoesService.getAll().subscribe((dados) => {
      this.transacoes = dados;
    });
  }
  operationType(value: string): string {
    return OperationType[value as keyof typeof OperationType];
  }

}
