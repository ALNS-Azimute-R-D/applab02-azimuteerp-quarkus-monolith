package de.org.dexterity.azimuteerp.monolith.quarkus.web.rest;

import static jakarta.ws.rs.core.UriBuilder.fromPath;

import de.org.dexterity.azimuteerp.monolith.quarkus.service.Paged;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.PaymentMethodService;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.PaymentMethodDTO;
import de.org.dexterity.azimuteerp.monolith.quarkus.web.rest.errors.BadRequestAlertException;
import de.org.dexterity.azimuteerp.monolith.quarkus.web.rest.vm.PageRequestVM;
import de.org.dexterity.azimuteerp.monolith.quarkus.web.rest.vm.SortRequestVM;
import de.org.dexterity.azimuteerp.monolith.quarkus.web.util.HeaderUtil;
import de.org.dexterity.azimuteerp.monolith.quarkus.web.util.PaginationUtil;
import de.org.dexterity.azimuteerp.monolith.quarkus.web.util.ResponseUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.Optional;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * REST controller for managing {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.PaymentMethod}.
 */
@Path("/api/payment-methods")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class PaymentMethodResource {

    private final Logger log = LoggerFactory.getLogger(PaymentMethodResource.class);

    private static final String ENTITY_NAME = "paymentMethod";

    @ConfigProperty(name = "application.name")
    String applicationName;

    @Inject
    PaymentMethodService paymentMethodService;

    /**
     * {@code POST  /payment-methods} : Create a new paymentMethod.
     *
     * @param paymentMethodDTO the paymentMethodDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new paymentMethodDTO, or with status {@code 400 (Bad Request)} if the paymentMethod has already an ID.
     */
    @POST
    public Response createPaymentMethod(@Valid PaymentMethodDTO paymentMethodDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save PaymentMethod : {}", paymentMethodDTO);
        if (paymentMethodDTO.id != null) {
            throw new BadRequestAlertException("A new paymentMethod cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = paymentMethodService.persistOrUpdate(paymentMethodDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /payment-methods} : Updates an existing paymentMethod.
     *
     * @param paymentMethodDTO the paymentMethodDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated paymentMethodDTO,
     * or with status {@code 400 (Bad Request)} if the paymentMethodDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentMethodDTO couldn't be updated.
     */
    @PUT
    @Path("/{id}")
    public Response updatePaymentMethod(@Valid PaymentMethodDTO paymentMethodDTO, @PathParam("id") Long id) {
        log.debug("REST request to update PaymentMethod : {}", paymentMethodDTO);
        if (paymentMethodDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = paymentMethodService.persistOrUpdate(paymentMethodDTO);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentMethodDTO.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /payment-methods/:id} : delete the "id" paymentMethod.
     *
     * @param id the id of the paymentMethodDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deletePaymentMethod(@PathParam("id") Long id) {
        log.debug("REST request to delete PaymentMethod : {}", id);
        paymentMethodService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /payment-methods} : get all the paymentMethods.
     *
     * @param pageRequest the pagination information.
     * @return the {@link Response} with status {@code 200 (OK)} and the list of paymentMethods in body.
     */
    @GET
    public Response getAllPaymentMethods(
        @BeanParam PageRequestVM pageRequest,
        @BeanParam SortRequestVM sortRequest,
        @Context UriInfo uriInfo
    ) {
        log.debug("REST request to get a page of PaymentMethods");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        Paged<PaymentMethodDTO> result = paymentMethodService.findAll(page);
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }

    /**
     * {@code GET  /payment-methods/:id} : get the "id" paymentMethod.
     *
     * @param id the id of the paymentMethodDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the paymentMethodDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")
    public Response getPaymentMethod(@PathParam("id") Long id) {
        log.debug("REST request to get PaymentMethod : {}", id);
        Optional<PaymentMethodDTO> paymentMethodDTO = paymentMethodService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentMethodDTO);
    }
}
