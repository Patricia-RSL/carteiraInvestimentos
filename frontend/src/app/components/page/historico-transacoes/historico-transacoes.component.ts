import { Component } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
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
  totalElements: number | undefined;
  totalPages: number = 0;
  currentPage: number = 0;
  size: number = 10;
  pageNumber: number = 0;


  constructor(private historicoTransacoesService: HistoricoTransacoesService) {}

  ngOnInit(): void {
    this.loadPage(this.currentPage, this.size);
  }

  loadPage(page: number, size: number): void {
    this.historicoTransacoesService.getAll(page, size).subscribe(response => {
      this.transacoes = response.content; 
      this.totalPages = response.totalPages; 
      this.currentPage = response.number; 
      this.totalElements = response.totalElements;
      this.pageNumber = response.number;
    });
  }

  onPageChange(event: PageEvent): void {
    this.size = event.pageSize;
    this.pageNumber = event.pageIndex;
    this.loadPage(this.pageNumber, this.size);
  }

  operationType(value: string): string {
    return OperationType[value as keyof typeof OperationType];
  }

}
