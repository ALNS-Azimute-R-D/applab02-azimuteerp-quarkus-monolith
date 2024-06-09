import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PaymentGatewayService } from '../service/payment-gateway.service';
import { IPaymentGateway } from '../payment-gateway.model';
import { PaymentGatewayFormService } from './payment-gateway-form.service';

import { PaymentGatewayUpdateComponent } from './payment-gateway-update.component';

describe('PaymentGateway Management Update Component', () => {
  let comp: PaymentGatewayUpdateComponent;
  let fixture: ComponentFixture<PaymentGatewayUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let paymentGatewayFormService: PaymentGatewayFormService;
  let paymentGatewayService: PaymentGatewayService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), PaymentGatewayUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PaymentGatewayUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PaymentGatewayUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    paymentGatewayFormService = TestBed.inject(PaymentGatewayFormService);
    paymentGatewayService = TestBed.inject(PaymentGatewayService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const paymentGateway: IPaymentGateway = { id: 456 };

      activatedRoute.data = of({ paymentGateway });
      comp.ngOnInit();

      expect(comp.paymentGateway).toEqual(paymentGateway);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPaymentGateway>>();
      const paymentGateway = { id: 123 };
      jest.spyOn(paymentGatewayFormService, 'getPaymentGateway').mockReturnValue(paymentGateway);
      jest.spyOn(paymentGatewayService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paymentGateway });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: paymentGateway }));
      saveSubject.complete();

      // THEN
      expect(paymentGatewayFormService.getPaymentGateway).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(paymentGatewayService.update).toHaveBeenCalledWith(expect.objectContaining(paymentGateway));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPaymentGateway>>();
      const paymentGateway = { id: 123 };
      jest.spyOn(paymentGatewayFormService, 'getPaymentGateway').mockReturnValue({ id: null });
      jest.spyOn(paymentGatewayService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paymentGateway: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: paymentGateway }));
      saveSubject.complete();

      // THEN
      expect(paymentGatewayFormService.getPaymentGateway).toHaveBeenCalled();
      expect(paymentGatewayService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPaymentGateway>>();
      const paymentGateway = { id: 123 };
      jest.spyOn(paymentGatewayService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paymentGateway });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(paymentGatewayService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
