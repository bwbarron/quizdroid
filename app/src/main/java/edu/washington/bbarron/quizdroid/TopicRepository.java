package edu.washington.bbarron.quizdroid;

import java.util.List;

public interface TopicRepository {

    public List<Topic> getAllTopics();

    public List<Topic> getTopicsByKeyword(String keyword);

}