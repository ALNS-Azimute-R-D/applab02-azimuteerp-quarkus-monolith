package org.dexterity.darueira.azimuteerp.monolith.quarkus.web.rest;

import static jakarta.ws.rs.core.UriBuilder.fromPath;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.Optional;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.Paged;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.ProvinceService;
import org.dexterity.darueira.azimuteerp.monolith.quarkus.service.dto.ProvinceDTO;
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
 * REST controller for managing {@link org.dexterity.darueira.azimuteerp.monolith.quarkus.domain.Province}.
 */
@Path("/api/provinces")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class ProvinceResource {

    private final Logger log = LoggerFactory.getLogger(ProvinceResource.class);

    private static final String ENTITY_NAME = "province";

    @ConfigProperty(name = "application.name")
    String applicationName;

    @Inject
    ProvinceService provinceService;

    /**
     * {@code POST  /provinces} : Create a new province.
     *
     * @param provinceDTO the provinceDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new provinceDTO, or with status {@code 400 (Bad Request)} if the province has already an ID.
     */
    @POST
    public Response createProvince(@Valid ProvinceDTO provinceDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save Province : {}", provinceDTO);
        if (provinceDTO.id != null) {
            throw new BadRequestAlertException("A new province cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = provinceService.persistOrUpdate(provinceDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /provinces} : Updates an existing province.
     *
     * @param provinceDTO the provinceDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated provinceDTO,
     * or with status {@code 400 (Bad Request)} if the provinceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the provinceDTO couldn't be updated.
     */
    @PUT
    @Path("/{id}")
    public Response updateProvince(@Valid ProvinceDTO provinceDTO, @PathParam("id") Long id) {
        log.debug("REST request to update Province : {}", provinceDTO);
        if (provinceDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = provinceService.persistOrUpdate(provinceDTO);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, provinceDTO.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /provinces/:id} : delete the "id" province.
     *
     * @param id the id of the provinceDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteProvince(@PathParam("id") Long id) {
        log.debug("REST request to delete Province : {}", id);
        provinceService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /provinces} : get all the provinces.
     *
     * @param pageRequest the pagination information.
     * @return the {@link Response} with status {@code 200 (OK)} and the list of provinces in body.
     */
    @GET
    public Response getAllProvinces(@BeanParam PageRequestVM pageRequest, @BeanParam SortRequestVM sortRequest, @Context UriInfo uriInfo) {
        log.debug("REST request to get a page of Provinces");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        Paged<ProvinceDTO> result = provinceService.findAll(page);
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }

    /**
     * {@code GET  /provinces/:id} : get the "id" province.
     *
     * @param id the id of the provinceDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the provinceDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")
    public Response getProvince(@PathParam("id") Long id) {
        log.debug("REST request to get Province : {}", id);
        Optional<ProvinceDTO> provinceDTO = provinceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(provinceDTO);
    }
}
