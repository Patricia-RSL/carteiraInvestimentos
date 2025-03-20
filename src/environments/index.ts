import { environment } from './environment';

export const endpoint = (path: string): string => {
  path = path.startsWith('/') ? path : `/${path}`;
  return !path ? environment.apiUrl : `${environment.apiUrl}${path}`.trim();
};