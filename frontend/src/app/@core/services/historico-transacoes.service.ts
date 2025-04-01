import { Injectable } from "@angular/core";
import {endpoint} from '../../../environments';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import { HistoricoFilter, PortfolioAnalysisRequestDTO, PortfolioAnalysisResponseDTO, UserTrade } from "../BackendModel";

@Injectable({
    providedIn: 'root',
  })
  export class HistoricoTransacoesService {
    private url = endpoint(`/api/usertrade`);
  
    constructor(private http: HttpClient) {}
  
    getAll(page: number, size: number, filtro: HistoricoFilter): Observable<any> {
      let params = new HttpParams()
        .set('page', page.toString())
        .set('size', size.toString());

      if (filtro.beginDate) {
        params = params.set('beginDate', filtro.beginDate);
      }
      if (filtro.endDate) {
        params = params.set('endDate', filtro.endDate);
      }
      if (filtro.instruments && filtro.instruments.length > 0) {
        params = params.set('instruments', filtro.instruments.join(','));
      }
  
      return this.http.get<any>(`${this.url}/paginated`, {
        headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
        params: params
      });
    }
  }