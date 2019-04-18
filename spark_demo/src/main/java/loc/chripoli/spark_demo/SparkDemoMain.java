package loc.chripoli.spark_demo;

import org.apache.commons.lang.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.net.InetAddress;
import java.util.Arrays;

/**
 * Basic Spark program that calls WireMock for some Integer values and afterwards
 * make some basic operations.
 *
 * @author chripoli
 */
public class SparkDemoMain {

    private static final Logger LOG = LoggerFactory.getLogger(SparkDemoMain.class);
    private static final String SPARK_MASTER_ENV_KEY = "SPARK_MASTER";
    private static final String WIREMOCK_URL_ENV_KEY = "WIREMOCK_URL";
    private static final String JARS_ENV_KEY = "JARS";
    private static final String APP_NAME = "Spark Demo with REST Calls";

    public static void main(String[] args) {

        final String master = System.getenv(SPARK_MASTER_ENV_KEY);
        final String wiremockUrl = System.getenv(WIREMOCK_URL_ENV_KEY);
        final String[] jars = System.getenv(JARS_ENV_KEY).split(";");

        final SparkConf config = new SparkConf();
        config.setMaster(master);
        config.setJars(jars);
        config.setAppName(APP_NAME);
        config.set("spark.dynamicAllocation.enabled", "false");
        config.set("spark.rdd.compress", "true");
        config.set("spark.executor.memory", "512M");

        JavaSparkContext sc = new JavaSparkContext(config);


        JavaRDD<Integer> counter = sc.parallelize(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));

        JavaRDD<Integer> result = counter.map((Function<Integer, Integer>) integer -> {

            final Client client = ClientBuilder.newClient();
            WebTarget target = client.target(wiremockUrl);
            Invocation.Builder invocationBuilder = target.request(MediaType.TEXT_PLAIN);
            final Integer intResponse = invocationBuilder.get(Integer.class);
            LOG.info("Received integer " + intResponse + " on worker " + InetAddress.getLocalHost());

            return intResponse;

        });
        JavaRDD<Integer> intCalcResult = result.map(
                (Function<Integer, Integer>) x -> x * x);
        LOG.info(String.valueOf(intCalcResult.count()));
        LOG.info(StringUtils.join(intCalcResult.collect(), ","));
    }
}
