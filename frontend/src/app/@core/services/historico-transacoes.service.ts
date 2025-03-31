import { Injectable } from "@angular/core";
import {endpoint} from '../../../environments';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import { PortfolioAnalysisRequestDTO, PortfolioAnalysisResponseDTO, UserTrade } from "../BackendModel";

@Injectable({
    providedIn: 'root',
  })
  export class HistoricoTransacoesService {
    private url = endpoint(`/api/usertrade`);
  
    constructor(private http: HttpClient) {}
  
    getAll(): Observable<UserTrade[]> {
        return this.http.get<UserTrade[]>(`${this.url}/`, {
            headers: new HttpHeaders({ 'Content-Type': 'application/json' })
        });
    }
  }