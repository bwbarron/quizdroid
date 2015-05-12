package edu.washington.bbarron.quizdroid;

import java.util.List;

public interface TopicRepository {

    public List<Topic> getAllTopics();

    public Topic getTopicByKeyword(String keyword);

}