import { Component, OnInit } from '@angular/core';
import { PortfolioAnalysisRequestDTO, PortfolioAnalysisResponseDTO, PortfolioAnalysisSummaryDTO } from "../../@core/BackendModel";
import { PortfolioService } from 'src/app/@core/services/portfolio.service';
import {FormControl} from '@angular/forms';


@Component({
  selector: 'portfolio',
  templateUrl: './portfolio.component.html',
  styleUrls: ['./portfolio.component.css']
})
export class PortfolioComponent implements OnInit{

  displayedColumns: String[] = ['instrument', 'qtdAcoes', 'valorInvestido', 'valorMercado', 'rendimento'];
  portfolio: PortfolioAnalysisResponseDTO = {} as PortfolioAnalysisResponseDTO;
  resumoPortfolio: PortfolioAnalysisSummaryDTO = {} as PortfolioAnalysisSummaryDTO;
  filtro: PortfolioAnalysisRequestDTO = {} as PortfolioAnalysisRequestDTO;
  instrumentsOptions: String[] = [];
  instrumentsControl = new FormControl([]);


  constructor( private portfolioService: PortfolioService){

  }
  ngOnInit(): void {
    this.portfolioService.getInstrumentList().subscribe((result)=>{
      this.instrumentsOptions = result;
    });

    this.portfolioService.getAnalisePortfolio(this.filtro).subscribe((result)=>{
      this.portfolio = result;
      this.resumoPortfolio = result.portfolioAnalysisSummaryDTO;
    });
  }

  updateSelectedInstruments() {
    this.filtro.instrumentList = this.instrumentsControl.value || [];
  }

  clearFilter(){
    this.filtro = {} as PortfolioAnalysisRequestDTO;
    this.instrumentsControl.setValue(null);
  }

  applyFilter() {
    this.portfolioService.getAnalisePortfolio(this.filtro).subscribe((result)=>{
      this.portfolio = result;
      this.resumoPortfolio = result.portfolioAnalysisSummaryDTO;
    });
  }
}
