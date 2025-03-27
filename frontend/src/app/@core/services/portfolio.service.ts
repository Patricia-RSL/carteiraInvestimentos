import { Injectable } from "@angular/core";
import {endpoint} from '../../../environments';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import { PortfolioAnalysisResponseDTO } from "../BackendModel";

@Injectable({
    providedIn: 'root',
  })
  export class PortfolioService {
    private url = endpoint(`/api/analise-carteira`);
  
    constructor(private http: HttpClient) {}
  
    getAnalisePortfolio(): Observable<PortfolioAnalysisResponseDTO> {
    const PortfolioAnalysisRequestDTO = {
        beginDate: "2019-01-17 00:00:00",
        endDate: "2019-12-17 00:00:00",
        instrumentList: []
        };
        return this.http.post<PortfolioAnalysisResponseDTO>(`${this.url}/analisar`, PortfolioAnalysisRequestDTO, {
            headers: new HttpHeaders({ 'Content-Type': 'application/json' })
        });
    }
  }