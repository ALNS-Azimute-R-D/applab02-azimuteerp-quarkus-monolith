jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { AssetCollectionService } from '../service/asset-collection.service';

import { AssetCollectionDeleteDialogComponent } from './asset-collection-delete-dialog.component';

describe('AssetCollection Management Delete Component', () => {
  let comp: AssetCollectionDeleteDialogComponent;
  let fixture: ComponentFixture<AssetCollectionDeleteDialogComponent>;
  let service: AssetCollectionService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, AssetCollectionDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(AssetCollectionDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AssetCollectionDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(AssetCollectionService);
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
