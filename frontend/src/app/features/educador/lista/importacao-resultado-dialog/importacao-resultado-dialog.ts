import { Component, inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { LinhaImportada, ResultadoImportacao } from '../../../../core/services/educando.service';

@Component({
  selector: 'app-importacao-resultado-dialog',
  imports: [MatButtonModule, MatDialogModule],
  templateUrl: './importacao-resultado-dialog.html',
  styleUrl: './importacao-resultado-dialog.scss',
})
export class ImportacaoResultadoDialog {
  protected readonly data = inject<ResultadoImportacao>(MAT_DIALOG_DATA);
  private readonly dialogRef = inject(MatDialogRef<ImportacaoResultadoDialog>);

  protected readonly sucesso: LinhaImportada[] = this.data.linhas.filter((l) => !l.erro);
  protected readonly erros: LinhaImportada[] = this.data.linhas.filter((l) => !!l.erro);

  confirmar(): void {
    this.dialogRef.close();
  }
}
