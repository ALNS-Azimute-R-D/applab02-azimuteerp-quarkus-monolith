package de.org.dexterity.azimuteerp.monolith.quarkus.web.rest;

import static jakarta.ws.rs.core.UriBuilder.fromPath;

import de.org.dexterity.azimuteerp.monolith.quarkus.service.BrandService;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.Paged;
import de.org.dexterity.azimuteerp.monolith.quarkus.service.dto.BrandDTO;
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
 * REST controller for managing {@link de.org.dexterity.azimuteerp.monolith.quarkus.domain.Brand}.
 */
@Path("/api/brands")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class BrandResource {

    private final Logger log = LoggerFactory.getLogger(BrandResource.class);

    private static final String ENTITY_NAME = "brand";

    @ConfigProperty(name = "application.name")
    String applicationName;

    @Inject
    BrandService brandService;

    /**
     * {@code POST  /brands} : Create a new brand.
     *
     * @param brandDTO the brandDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new brandDTO, or with status {@code 400 (Bad Request)} if the brand has already an ID.
     */
    @POST
    public Response createBrand(@Valid BrandDTO brandDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save Brand : {}", brandDTO);
        if (brandDTO.id != null) {
            throw new BadRequestAlertException("A new brand cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = brandService.persistOrUpdate(brandDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /brands} : Updates an existing brand.
     *
     * @param brandDTO the brandDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated brandDTO,
     * or with status {@code 400 (Bad Request)} if the brandDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the brandDTO couldn't be updated.
     */
    @PUT
    @Path("/{id}")
    public Response updateBrand(@Valid BrandDTO brandDTO, @PathParam("id") Long id) {
        log.debug("REST request to update Brand : {}", brandDTO);
        if (brandDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = brandService.persistOrUpdate(brandDTO);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, brandDTO.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /brands/:id} : delete the "id" brand.
     *
     * @param id the id of the brandDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteBrand(@PathParam("id") Long id) {
        log.debug("REST request to delete Brand : {}", id);
        brandService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /brands} : get all the brands.
     *
     * @param pageRequest the pagination information.
     * @return the {@link Response} with status {@code 200 (OK)} and the list of brands in body.
     */
    @GET
    public Response getAllBrands(@BeanParam PageRequestVM pageRequest, @BeanParam SortRequestVM sortRequest, @Context UriInfo uriInfo) {
        log.debug("REST request to get a page of Brands");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        Paged<BrandDTO> result = brandService.findAll(page);
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }

    /**
     * {@code GET  /brands/:id} : get the "id" brand.
     *
     * @param id the id of the brandDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the brandDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")
    public Response getBrand(@PathParam("id") Long id) {
        log.debug("REST request to get Brand : {}", id);
        Optional<BrandDTO> brandDTO = brandService.findOne(id);
        return ResponseUtil.wrapOrNotFound(brandDTO);
    }
}
