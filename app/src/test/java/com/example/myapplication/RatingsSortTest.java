package com.example.myapplication;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

public class RatingsSortTest {

    private void sortRatings(List<CourseRatingInstance> ratingsList) {
        // Sorting logic remains the same
        ratingsList.sort((rating1, rating2) -> {
            int upvotes1 = (rating1.getUpvotes() != null) ? rating1.getUpvotes() : 0;
            int upvotes2 = (rating2.getUpvotes() != null) ? rating2.getUpvotes() : 0;

            if (upvotes1 != upvotes2) {
                return Integer.compare(upvotes2, upvotes1); // Descending order of upvotes
            }

            int commentLength1 = rating1.getComments() != null ? rating1.getComments().length() : 0;
            int commentLength2 = rating2.getComments() != null ? rating2.getComments().length() : 0;
            return Integer.compare(commentLength2, commentLength1); // Descending order of comment length
        });
    }

    @Test
    public void testSortRatings() {
        // Create a list of CourseRatingInstance objects with different upvotes and comment lengths
        CourseRatingInstance rating1 = new CourseRatingInstance();
        rating1.setUpvotes(5);
        rating1.setComments("Short comment");

        CourseRatingInstance rating2 = new CourseRatingInstance();
        rating2.setUpvotes(3);
        rating2.setComments("Longer comment with more characters");

        CourseRatingInstance rating3 = new CourseRatingInstance();
        rating3.setUpvotes(5);
        rating3.setComments("Medium comment");

        // Create a list and add the ratings in an unsorted order
        List<CourseRatingInstance> ratingsList = new ArrayList<>();
        ratingsList.add(rating1);
        ratingsList.add(rating2);
        ratingsList.add(rating3);

        System.out.println("Before sorting:");
        for (CourseRatingInstance rating : ratingsList) {
            System.out.println("Upvotes: " + rating.getUpvotes() + ", Comment Length: " + rating.getComments().length());
        }

        // Call the sorting method
        sortRatings(ratingsList);

        System.out.println("After sorting:");
        for (CourseRatingInstance rating : ratingsList) {
            System.out.println("Upvotes: " + rating.getUpvotes() + ", Comment Length: " + rating.getComments().length());
        }

        // Check if the ratings are sorted correctly (descending order by upvotes and then by comment length)
        assertEquals("First rating should be #3 (same upvotes, longer comment)", rating3, ratingsList.get(0));
        assertEquals("Second rating should be #1 (same upvotes, shorter comment", rating1, ratingsList.get(1));
        assertEquals("Third rating should be #2 (least upvotes)", rating2, ratingsList.get(2));
    }
}
