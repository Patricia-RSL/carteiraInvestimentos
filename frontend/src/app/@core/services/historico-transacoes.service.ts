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
  
    getAll(page: number, size: number): Observable<any> {
      const params = {
        page: page.toString(),
        size: size.toString()
      };
  
      return this.http.get<any>(`${this.url}/paginated`, {
        headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
        params: params
      });
    }
  }