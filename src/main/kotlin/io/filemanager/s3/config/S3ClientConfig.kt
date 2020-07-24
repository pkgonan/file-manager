package io.filemanager.s3.config

import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.http.async.SdkAsyncHttpClient
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.services.s3.S3Configuration
import software.amazon.awssdk.utils.StringUtils
import java.net.URI
import java.time.Duration

@Configuration
@EnableConfigurationProperties(S3ClientConfig.S3ClientConfigurationProperties::class)
class S3ClientConfig(
    private val properties: S3ClientConfigurationProperties
) {

    companion object {
        val LOG = LoggerFactory.getLogger(S3ClientConfig::class.java)!!
    }

    @Bean
    fun s3AsyncClient(): S3AsyncClient {
        val clientBuilder = S3AsyncClient.builder()
            .httpClient(getHttpClient())
            .serviceConfiguration(getServiceConfiguration())
            .region(getS3Region())
            .credentialsProvider(getS3CredentialProvider())

        if (properties.endpoint != null) {
            clientBuilder.endpointOverride(properties.endpoint)
        }

        return clientBuilder.build()
    }

    private fun getHttpClient(): SdkAsyncHttpClient {
        return NettyNioAsyncHttpClient.builder()
            .writeTimeout(Duration.ZERO)
            .maxConcurrency(64)
            .build()
    }

    private fun getServiceConfiguration(): S3Configuration {
        return S3Configuration.builder()
            .checksumValidationEnabled(false)
            .chunkedEncodingEnabled(true)
            .build()
    }

    private fun getS3Region(): Region {
        return properties.region
    }

    private fun getS3CredentialProvider(): AwsCredentialsProvider {
        if (StringUtils.isBlank(properties.accessKeyId) || StringUtils.isBlank(properties.secretAccessKey)) {
            LOG.warn("Invalid S3 Credential Configuration Properties : {}", properties)
            return DefaultCredentialsProvider.create()
        }
        return AwsCredentialsProvider { AwsBasicCredentials.create(properties.accessKeyId, properties.secretAccessKey) }
    }

    @ConfigurationProperties("aws.s3")
    class S3ClientConfigurationProperties {
        var region: Region = Region.US_EAST_1
        var endpoint: URI? = null
        var accessKeyId: String = ""
        var secretAccessKey: String = ""
        var bucket: String = ""

        // AWS S3 requires that file parts must have at least 5MB, except
        // for the last part. This may change for other S3-compatible services, so let't
        // define a configuration property for that
        var multipartMinPartSize: Int = 5 * 1024 * 1024

        override fun toString(): String {
            return "S3ClientConfigurationProperties(" +
                "region=$region, " +
                "endpoint=$endpoint, " +
                "accessKeyId=$accessKeyId, " +
                "secretAccessKey=$secretAccessKey, " +
                "bucket=$bucket" +
                ")"
        }
    }
}