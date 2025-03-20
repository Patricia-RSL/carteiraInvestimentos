import { Injectable } from "@angular/core";
import {endpoint} from '../../../environments';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import { AnaliseCarteiraResponseDTO } from "../BackendModel";

@Injectable({
    providedIn: 'root',
  })
  export class PortfolioService {
    private url = endpoint(`/api/analise-carteira`);
  
    constructor(private http: HttpClient) {}
  
    getAnalisePortfolio(): Observable<AnaliseCarteiraResponseDTO> {
    const AnaliseCarteiraRequestDTO = {
        dataInicio: "2019-01-17",
        dataFim: "2019-12-17",
        instrumentList: []
        };
        return this.http.post<AnaliseCarteiraResponseDTO>(`${this.url}/analisar`, AnaliseCarteiraRequestDTO, {
            headers: new HttpHeaders({ 'Content-Type': 'application/json' })
        });
    }
  }