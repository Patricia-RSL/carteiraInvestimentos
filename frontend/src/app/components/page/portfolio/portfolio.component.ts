import { Component, OnInit } from '@angular/core';
import { PortfolioAnalysisRequestDTO, PortfolioAnalysisResponseDTO, PortfolioAnalysisSummaryDTO } from "../../../@core/BackendModel";
import { PortfolioService } from 'src/app/@core/services/portfolio.service';
import {FormControl} from '@angular/forms';
import { ErrorModalComponent } from '../../error-modal/error-modal.component';
import { MatDialog } from '@angular/material/dialog';

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
  maxDate: Date = new Date();
  minDate: Date = new Date();
	beginDate: Date | null = null;
	endDate: Date | null = null;
  spinnerShow: boolean = true;


  constructor( private portfolioService: PortfolioService,
    private dialog: MatDialog
  ){

  }
  ngOnInit(): void {
    this.minDate.setMonth(this.maxDate.getMonth()-3);

		this.portfolioService.getInstrumentList().subscribe((result)=>{
      this.instrumentsOptions = result;
    });

    this.analyze();
  }

  updateSelectedInstruments() {
    this.filtro.instrumentList = this.instrumentsControl.value || [];
  }

  clearFilter(){
    this.filtro = {} as PortfolioAnalysisRequestDTO;
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

    this.analyze();
    
  }

  analyze(){
    this.spinnerShow = true;
    this.portfolioService.getAnalisePortfolio(this.filtro).subscribe(
      (result)=>{
      this.portfolio = result;
      this.resumoPortfolio = result.portfolioAnalysisSummaryDTO;
      this.spinnerShow=false;
    },
    (error)=>{
      this.spinnerShow=false;
      console.log(error)
      this.dialog.open(ErrorModalComponent, {
        data: error.error
      });
    })
}
}