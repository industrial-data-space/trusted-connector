package de.fhg.aisec.ids.idscp2.default_drivers.secure_channel.tlsv1_3

import de.fhg.aisec.ids.idscp2.default_drivers.secure_channel.tlsv1_3.client.TLSClient
import de.fhg.aisec.ids.idscp2.default_drivers.secure_channel.tlsv1_3.server.TLSServer
import de.fhg.aisec.ids.idscp2.idscp_core.drivers.SecureChannelDriver
import de.fhg.aisec.ids.idscp2.idscp_core.drivers.SecureServer
import de.fhg.aisec.ids.idscp2.idscp_core.error.Idscp2Exception
import de.fhg.aisec.ids.idscp2.idscp_core.api.idscp_connection.Idscp2Connection
import de.fhg.aisec.ids.idscp2.idscp_core.api.configuration.Idscp2Configuration
import de.fhg.aisec.ids.idscp2.idscp_core.api.idscp_server.SecureChannelInitListener
import de.fhg.aisec.ids.idscp2.idscp_core.secure_channel.SecureChannel
import de.fhg.aisec.ids.idscp2.idscp_core.api.idscp_server.ServerConnectionListener
import org.slf4j.LoggerFactory
import java.io.IOException
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.util.concurrent.CompletableFuture

/**
 * An implementation of SecureChannelDriver interface on TLSv1.3
 *
 * @author Leon Beckmann (leon.beckmann@aisec.fraunhofer.de)
 */
class NativeTLSDriver<CC: Idscp2Connection> : SecureChannelDriver<CC, NativeTlsConfiguration> {
    /**
     * Performs an asynchronous client connect to a TLS server.
     */
    override fun connect(connectionFactory: (SecureChannel, Idscp2Configuration) -> CC,
                         configuration: Idscp2Configuration,
                         secureChannelConfig: NativeTlsConfiguration): CompletableFuture<CC> {
        val connectionFuture = CompletableFuture<CC>()
        try {
            if (LOG.isTraceEnabled) {
                LOG.trace("Create TLS client and connect to server")
            }
            val tlsClient = TLSClient(connectionFactory, configuration, secureChannelConfig, connectionFuture)
            tlsClient.connect(secureChannelConfig.host, secureChannelConfig.serverPort)
        } catch (e: IOException) {
            connectionFuture.completeExceptionally(Idscp2Exception("Call to connect() has failed", e))
        } catch (e: NoSuchAlgorithmException) {
            connectionFuture.completeExceptionally(Idscp2Exception("Call to connect() has failed", e))
        } catch (e: KeyManagementException) {
            connectionFuture.completeExceptionally(Idscp2Exception("Call to connect() has failed", e))
        }
        return connectionFuture
    }

    /**
     * Creates and starts a new TLS Server instance.
     *
     * @return The SecureServer instance
     * @throws Idscp2Exception If any error occurred during server creation/start
     */
    override fun listen(channelInitListener: SecureChannelInitListener<CC>,
                        serverListenerPromise: CompletableFuture<ServerConnectionListener<CC>>,
                        secureChannelConfig: NativeTlsConfiguration): SecureServer {
        return try {
            if (LOG.isTraceEnabled) {
                LOG.trace("Create TLS server")
            }
            TLSServer(secureChannelConfig, channelInitListener, serverListenerPromise)
        } catch (e: IOException) {
            throw Idscp2Exception("Error while trying to to start SecureServer", e)
        } catch (e: NoSuchAlgorithmException) {
            throw Idscp2Exception("Error while trying to to start SecureServer", e)
        } catch (e: KeyManagementException) {
            throw Idscp2Exception("Error while trying to to start SecureServer", e)
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(NativeTLSDriver::class.java)
    }
}