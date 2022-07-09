package cn.ywrby.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Log {
    private int userId;
    private int bookId;
    private int book_selection_click;
    private int book_read_time;
    private int book_exam_click;
}
