package org.dexterity.darueira.azimuteerp.monolith.quarkus.web.rest;

import static jakarta.ws.rs.core.UriBuilder.fromPath;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.Optional;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.Paged;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.PaymentGatewayService;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.PaymentGatewayDTO;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.web.rest.errors.BadRequestAlertException;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.web.rest.vm.PageRequestVM;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.web.rest.vm.SortRequestVM;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.web.util.HeaderUtil;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.web.util.PaginationUtil;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.web.util.ResponseUtil;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * REST controller for managing {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.PaymentGateway}.
 */
@Path("/api/payment-gateways")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class PaymentGatewayResource {

    private final Logger log = LoggerFactory.getLogger(PaymentGatewayResource.class);

    private static final String ENTITY_NAME = "paymentGateway";

    @ConfigProperty(name = "application.name")
    String applicationName;

    @Inject
    PaymentGatewayService paymentGatewayService;

    /**
     * {@code POST  /payment-gateways} : Create a new paymentGateway.
     *
     * @param paymentGatewayDTO the paymentGatewayDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new paymentGatewayDTO, or with status {@code 400 (Bad Request)} if the paymentGateway has already an ID.
     */
    @POST
    public Response createPaymentGateway(@Valid PaymentGatewayDTO paymentGatewayDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save PaymentGateway : {}", paymentGatewayDTO);
        if (paymentGatewayDTO.id != null) {
            throw new BadRequestAlertException("A new paymentGateway cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = paymentGatewayService.persistOrUpdate(paymentGatewayDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /payment-gateways} : Updates an existing paymentGateway.
     *
     * @param paymentGatewayDTO the paymentGatewayDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated paymentGatewayDTO,
     * or with status {@code 400 (Bad Request)} if the paymentGatewayDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentGatewayDTO couldn't be updated.
     */
    @PUT
    @Path("/{id}")
    public Response updatePaymentGateway(@Valid PaymentGatewayDTO paymentGatewayDTO, @PathParam("id") Long id) {
        log.debug("REST request to update PaymentGateway : {}", paymentGatewayDTO);
        if (paymentGatewayDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = paymentGatewayService.persistOrUpdate(paymentGatewayDTO);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentGatewayDTO.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /payment-gateways/:id} : delete the "id" paymentGateway.
     *
     * @param id the id of the paymentGatewayDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deletePaymentGateway(@PathParam("id") Long id) {
        log.debug("REST request to delete PaymentGateway : {}", id);
        paymentGatewayService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /payment-gateways} : get all the paymentGateways.
     *
     * @param pageRequest the pagination information.
     * @return the {@link Response} with status {@code 200 (OK)} and the list of paymentGateways in body.
     */
    @GET
    public Response getAllPaymentGateways(
        @BeanParam PageRequestVM pageRequest,
        @BeanParam SortRequestVM sortRequest,
        @Context UriInfo uriInfo
    ) {
        log.debug("REST request to get a page of PaymentGateways");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        Paged<PaymentGatewayDTO> result = paymentGatewayService.findAll(page);
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }

    /**
     * {@code GET  /payment-gateways/:id} : get the "id" paymentGateway.
     *
     * @param id the id of the paymentGatewayDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the paymentGatewayDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")
    public Response getPaymentGateway(@PathParam("id") Long id) {
        log.debug("REST request to get PaymentGateway : {}", id);
        Optional<PaymentGatewayDTO> paymentGatewayDTO = paymentGatewayService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentGatewayDTO);
    }
}
