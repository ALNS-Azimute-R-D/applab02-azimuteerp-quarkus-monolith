package org.dexterity.darueira.azimuteerp.monolith.quarkus.web.rest;

import static jakarta.ws.rs.core.UriBuilder.fromPath;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.Optional;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.CustomerTypeService;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.Paged;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.CustomerTypeDTO;
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
 * REST controller for managing {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.CustomerType}.
 */
@Path("/api/customer-types")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class CustomerTypeResource {

    private final Logger log = LoggerFactory.getLogger(CustomerTypeResource.class);

    private static final String ENTITY_NAME = "customerType";

    @ConfigProperty(name = "application.name")
    String applicationName;

    @Inject
    CustomerTypeService customerTypeService;

    /**
     * {@code POST  /customer-types} : Create a new customerType.
     *
     * @param customerTypeDTO the customerTypeDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new customerTypeDTO, or with status {@code 400 (Bad Request)} if the customerType has already an ID.
     */
    @POST
    public Response createCustomerType(@Valid CustomerTypeDTO customerTypeDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save CustomerType : {}", customerTypeDTO);
        if (customerTypeDTO.id != null) {
            throw new BadRequestAlertException("A new customerType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = customerTypeService.persistOrUpdate(customerTypeDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /customer-types} : Updates an existing customerType.
     *
     * @param customerTypeDTO the customerTypeDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated customerTypeDTO,
     * or with status {@code 400 (Bad Request)} if the customerTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customerTypeDTO couldn't be updated.
     */
    @PUT
    @Path("/{id}")
    public Response updateCustomerType(@Valid CustomerTypeDTO customerTypeDTO, @PathParam("id") Long id) {
        log.debug("REST request to update CustomerType : {}", customerTypeDTO);
        if (customerTypeDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = customerTypeService.persistOrUpdate(customerTypeDTO);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerTypeDTO.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /customer-types/:id} : delete the "id" customerType.
     *
     * @param id the id of the customerTypeDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteCustomerType(@PathParam("id") Long id) {
        log.debug("REST request to delete CustomerType : {}", id);
        customerTypeService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /customer-types} : get all the customerTypes.
     *
     * @param pageRequest the pagination information.
     * @return the {@link Response} with status {@code 200 (OK)} and the list of customerTypes in body.
     */
    @GET
    public Response getAllCustomerTypes(
        @BeanParam PageRequestVM pageRequest,
        @BeanParam SortRequestVM sortRequest,
        @Context UriInfo uriInfo
    ) {
        log.debug("REST request to get a page of CustomerTypes");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        Paged<CustomerTypeDTO> result = customerTypeService.findAll(page);
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }

    /**
     * {@code GET  /customer-types/:id} : get the "id" customerType.
     *
     * @param id the id of the customerTypeDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the customerTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")
    public Response getCustomerType(@PathParam("id") Long id) {
        log.debug("REST request to get CustomerType : {}", id);
        Optional<CustomerTypeDTO> customerTypeDTO = customerTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customerTypeDTO);
    }
}
