package com.ciandt.pms.integration.queue;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.ciandt.pms.Constants;
import com.ciandt.pms.resource.SQSMessageResource;
import com.google.gson.Gson;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Properties;


@Component
public class PendingMailProducer {

    private static final Logger logger = LogManager.getLogger(PendingMailProducer.class.getName());

    private static final String APPLICATION = "PMS";

    private AmazonSQS sqs;
    private String queueUrl;
    private String key;
    private String secretKey;
    private String awsRegion;

    @Autowired
    private Properties appConfigBean;

    @PostConstruct
    public void init() {
        this.queueUrl = appConfigBean.getProperty(Constants.AWS_MAIL_QUEUE_URL);
        this.key = appConfigBean.getProperty(Constants.AWS_MAIL_KEY);
        this.secretKey = appConfigBean.getProperty(Constants.AWS_MAIL_SECRET_KEY);
        this.awsRegion = appConfigBean.getProperty(Constants.AWS_MAIL_REGION);
        this.sqs = AmazonSQSClient.builder()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(key, secretKey)))
                .withRegion(awsRegion)
                .build();
    }

    public void produce(final SQSMessageResource resource, final String to, final String cc, final String bcc) {
        logger.log(Level.INFO, "Creating a mail message with subject: {}", resource.getSubject());

        try {
            sqs.sendMessage(new SendMessageRequest(queueUrl, toJson(resource)));
        } catch (final AmazonServiceException ase) {
            logger.log(Level.ERROR, "Caught an AmazonServiceException, which means " +
                    "your request made it to Amazon SQS, but was " +
                    "rejected with an error response for some reason.");
            logger.log(Level.ERROR, "Error Message:    " + ase.getMessage());
            logger.log(Level.ERROR, "HTTP Status Code: " + ase.getStatusCode());
            logger.log(Level.ERROR, "AWS Error Code:   " + ase.getErrorCode());
            logger.log(Level.ERROR, "Error Type:       " + ase.getErrorType());
            logger.log(Level.ERROR, "Request ID:       " + ase.getRequestId());
        } catch (final AmazonClientException ace) {
            logger.log(Level.ERROR, "Caught an AmazonClientException, which means " +
                    "the client encountered a serious internal problem while " +
                    "trying to communicate with Amazon SQS, such as not " +
                    "being able to access the network.");
            logger.log(Level.ERROR, "Error Message: " + ace.getMessage());
        } catch (final Exception e) {
            logger.log(Level.ERROR, "Error sending an mail with subject  : " + resource.getSubject());
            logger.log(Level.ERROR, "                           sender   : " + resource.getFrom());
            logger.log(Level.ERROR, "                           to       : " + resource.getTo());
            logger.log(Level.ERROR, "                           cc       : " + resource.getCc());
            logger.log(Level.ERROR, "                           reply to : " + resource.getReplyTo());
            logger.log(Level.ERROR, "                           content  : " + resource.getHtml());
            logger.log(Level.ERROR, "                           Exception: " + e.getMessage());
        }
    }

    private String toJson(SQSMessageResource resource) {
        return new Gson().toJson(resource);
    }

}
