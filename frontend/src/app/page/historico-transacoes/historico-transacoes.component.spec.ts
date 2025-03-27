import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HistoricoTransacoesComponent } from './historico-transacoes.component';

describe('HistoricoTransacoesComponent', () => {
  let component: HistoricoTransacoesComponent;
  let fixture: ComponentFixture<HistoricoTransacoesComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [HistoricoTransacoesComponent]
    });
    fixture = TestBed.createComponent(HistoricoTransacoesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
