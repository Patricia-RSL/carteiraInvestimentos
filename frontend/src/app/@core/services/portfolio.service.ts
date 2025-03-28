import { Injectable } from "@angular/core";
import {endpoint} from '../../../environments';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import { PortfolioAnalysisRequestDTO, PortfolioAnalysisResponseDTO } from "../BackendModel";

@Injectable({
    providedIn: 'root',
  })
  export class PortfolioService {
    private url = endpoint(`/api/analise-carteira`);
  
    constructor(private http: HttpClient) {}
  
    getAnalisePortfolio(filtro: PortfolioAnalysisRequestDTO): Observable<PortfolioAnalysisResponseDTO> {
        return this.http.post<PortfolioAnalysisResponseDTO>(`${this.url}/analisar`, filtro, {
            headers: new HttpHeaders({ 'Content-Type': 'application/json' })
        });
    }

    getInstrumentList(): Observable<String[]> {
      return this.http.get<String[]>(`${this.url}/instruments`,{
        headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    });
  }
  }