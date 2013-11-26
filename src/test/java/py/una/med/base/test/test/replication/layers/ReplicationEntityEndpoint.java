/*
 * @ReplicationEntityEndpoint.java 1.0 Nov 8, 2013 Sistema Integral de Gestion
 * Hospitalaria
 */
package py.una.med.base.test.test.replication.layers;

import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import py.una.med.base.replication.server.AbstractReplicationEndpoint;
import py.una.med.base.replication.server.Bundle;
import py.una.med.base.services.server.WebServiceDefinition;

/**
 * 
 * @author Arturo Volpe
 * @since 2.2.8
 * @version 1.0 Nov 8, 2013
 * 
 */
@WebServiceDefinition(xsds = "")
public class ReplicationEntityEndpoint extends
		AbstractReplicationEndpoint<ReplicatedEntity, ReplicatedEntity> {

	@PayloadRoot(localPart = "replicationEntityRequest", namespace = "http://sigh.med.una.py/2013/schemas/test")
	@ResponsePayload
	public ReplicationEntityResponse replicationEntityRequest(
			@RequestPayload ReplicationEntityRequest request) {

		ReplicationEntityResponse toRet = new ReplicationEntityResponse();
		Bundle<ReplicatedEntity> bundle = getChanges(request.getLastId());
		toRet.setEntities(bundle.getEntities());
		toRet.setLastId(bundle.getLastId());
		return toRet;
	}
}
