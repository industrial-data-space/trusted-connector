package de.fhg.aisec.ids.idscp2.example

import de.fhg.aisec.ids.idscp2.drivers.default_driver_impl.rat.dummy.RatProverDummy
import de.fhg.aisec.ids.idscp2.drivers.default_driver_impl.rat.dummy.RatVerifierDummy
import de.fhg.aisec.ids.idscp2.idscp_core.configuration.AttestationConfig
import de.fhg.aisec.ids.idscp2.idscp_core.configuration.Idscp2Configuration
import java.nio.file.Paths
import java.util.*

object RunTLSServer {
    @JvmStatic
    fun main(argv: Array<String>) {

        val localAttestationConfig = AttestationConfig.Builder()
                .setSupportedRatSuite(arrayOf(RatProverDummy.RAT_PROVER_DUMMY_ID))
                .setExpectedRatSuite(arrayOf(RatVerifierDummy.RAT_VERIFIER_DUMMY_ID))
                .setRatTimeoutDelay(300 * 1000) // 300 seconds
                .build()

        val settings = Idscp2Configuration.Builder()
                .setKeyStorePath(Paths.get(Objects.requireNonNull(RunTLSServer::class.java.classLoader.getResource("ssl/aisecconnector1-keystore.p12")).path))
                .setTrustStorePath(Paths.get(Objects.requireNonNull(RunTLSServer::class.java.classLoader.getResource("ssl/client-truststore_new.p12")).path))
                .setCertificateAlias("1.0.1")
                .setDapsKeyAlias("1")
                .setAttestationConfig(localAttestationConfig)
                .build()

        val initiator = Idscp2ServerInitiator()
        initiator.init(settings)
    }
}