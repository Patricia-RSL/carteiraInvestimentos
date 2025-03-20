export interface AnaliseCarteiraResponseDTO {
    resumoAnaliseCarteiraDTO: ResumoAnaliseCarteiraDTO;
    detalhesAnaliseCarteiraDTO: ItemDetalhesAnaliseCarteiraDTO[];
  }
  
  export interface ItemDetalhesAnaliseCarteiraDTO {
    instrument: string;
    qtdAcoes: number;
    valorInvestido: number;
    valorMercado: number;
    gain: number;
    rendimentosPorcentagem: number;
  }
  
  export interface ResumoAnaliseCarteiraDTO {
    totalAcoes: number;
    valorInvestido: number; 
    valorMercado: number;
    rendimentosPorcentagem: number;
  }