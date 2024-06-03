jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { RawAssetProcTmpService } from '../service/raw-asset-proc-tmp.service';

import { RawAssetProcTmpDeleteDialogComponent } from './raw-asset-proc-tmp-delete-dialog.component';

describe('RawAssetProcTmp Management Delete Component', () => {
  let comp: RawAssetProcTmpDeleteDialogComponent;
  let fixture: ComponentFixture<RawAssetProcTmpDeleteDialogComponent>;
  let service: RawAssetProcTmpService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RawAssetProcTmpDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(RawAssetProcTmpDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RawAssetProcTmpDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(RawAssetProcTmpService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      }),
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
