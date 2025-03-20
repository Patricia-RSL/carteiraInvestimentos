import { Component, OnInit } from '@angular/core';
import { AnaliseCarteiraResponseDTO, ResumoAnaliseCarteiraDTO } from "../../@core/BackendModel";
import { PortfolioService } from 'src/app/@core/services/portfolio.service';


@Component({
  selector: 'portfolio',
  templateUrl: './portfolio.component.html',
  styleUrls: ['./portfolio.component.css']
})
export class PortfolioComponent implements OnInit{

  displayedColumns: String[] = ['instrument', 'qtdAcoes', 'valorInvestido', 'valorMercado', 'rendimento'];
  portfolio: AnaliseCarteiraResponseDTO = {} as AnaliseCarteiraResponseDTO;
  resumoPortfolio: ResumoAnaliseCarteiraDTO = {} as ResumoAnaliseCarteiraDTO;

  constructor( private portfolioService: PortfolioService){

  }
  ngOnInit(): void {
    console.log('oi')
    this.portfolioService.getAnalisePortfolio().subscribe((result)=>{
      this.portfolio = result;
      this.resumoPortfolio = result.resumoAnaliseCarteiraDTO;
    })
  }
}
