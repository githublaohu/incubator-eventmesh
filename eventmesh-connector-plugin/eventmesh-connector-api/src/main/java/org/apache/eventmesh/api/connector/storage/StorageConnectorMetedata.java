package org.apache.eventmesh.api.connector.storage;

import org.apache.eventmesh.api.connector.storage.data.ConsumerGroupInfo;
import org.apache.eventmesh.api.connector.storage.data.PullRequest;
import org.apache.eventmesh.api.connector.storage.data.TopicInfo;

import java.util.List;
import java.util.Set;

public interface StorageConnectorMetedata {

	public Set<String> getTopic() throws Exception;

	public List<ConsumerGroupInfo> getConsumerGroupInfo() throws Exception;

	public List<TopicInfo> geTopicInfos(List<PullRequest> pullRequests) throws Exception;

	public int createTopic(TopicInfo topicInfo) throws Exception;

	public int createConsumerGroupInfo(ConsumerGroupInfo consumerGroupInfo) throws Exception;

}
