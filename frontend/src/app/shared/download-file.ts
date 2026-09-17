export function baixarArquivo(blob: Blob, nomeArquivo: string): void {
  const url = window.URL.createObjectURL(blob);
  const link = document.createElement('a');
  link.href = url;
  link.download = nomeArquivo;
  link.click();
  window.URL.revokeObjectURL(url);
}
