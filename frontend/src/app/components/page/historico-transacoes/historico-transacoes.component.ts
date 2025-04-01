import { Component } from '@angular/core';
import { FormControl } from '@angular/forms';
import { PageEvent } from '@angular/material/paginator';
import { HistoricoFilter, OperationType, UserTrade } from 'src/app/@core/BackendModel';
import { HistoricoTransacoesService } from 'src/app/@core/services/historico-transacoes.service';
import { PortfolioService } from 'src/app/@core/services/portfolio.service';

@Component({
  selector: 'app-historico-transacoes',
  templateUrl: './historico-transacoes.component.html',
  styleUrls: ['./historico-transacoes.component.css']
})
export class HistoricoTransacoesComponent {
  transacoes: UserTrade[] = [];
  filtro: HistoricoFilter = {};
  displayedColumns: string[] = ['data', 'simbolo', 'tipo', 'preco', 'quantidade', 'total'];
  totalElements: number | undefined;
  totalPages: number = 0;
  currentPage: number = 0;
  size: number = 10;
  pageNumber: number = 0;
	beginDate: Date | null = null;
	endDate: Date | null = null;
  spinnerShow: boolean = true;
  instrumentsControl = new FormControl([]);
  instrumentsOptions: String[] = [];
  


  constructor(private historicoTransacoesService: HistoricoTransacoesService, private portfolioService: PortfolioService) {}

  ngOnInit(): void {
    this.portfolioService.getInstrumentList().subscribe((result)=>{
      this.instrumentsOptions = result;
    });
    this.loadPage(this.currentPage, this.size);
  }

  loadPage(page: number, size: number): void {
    this.historicoTransacoesService.getAll(page, size, this.filtro).subscribe(response => {
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

  clearFilter(){
      this.filtro = {};
      this.endDate = null;
      this.beginDate = null;
      this.instrumentsControl.setValue(null);
    }

    applyFilter() {
      if(this.beginDate!=null){

        this.filtro.beginDate = this.beginDate.toISOString().split('T')[0];
      }
  
      if(this.endDate!=null){
        this.filtro.endDate = this.endDate.toISOString().split('T')[0];
  
      }
      this.filtro.instruments = this.instrumentsControl.value || [];
      this.loadPage(this.currentPage, this.size);        
    }

}
