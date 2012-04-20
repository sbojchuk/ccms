package com.diploma.ccms.web.controller;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;

import com.diploma.ccms.domain.Blog;
import com.diploma.ccms.domain.BlogComment;
import com.diploma.ccms.domain.Calendar;
import com.diploma.ccms.domain.Contact;
import com.diploma.ccms.domain.Document;
import com.diploma.ccms.domain.DocumentCategory;
import com.diploma.ccms.domain.Message;
import com.diploma.ccms.domain.Note;
import com.diploma.ccms.domain.Region;
import com.diploma.ccms.domain.Room;
import com.diploma.ccms.domain.RoomCalendar;
import com.diploma.ccms.domain.Team;
import com.diploma.ccms.domain.Todo;
import com.diploma.ccms.domain.TodoCategory;
import com.diploma.ccms.domain.Wiki;
import com.diploma.ccms.domain.WikiCategory;
import com.diploma.ccms.domain.Worker;
import com.diploma.ccms.domain.WorkerJobType;
import com.diploma.ccms.domain.WorkerRole;

@Configurable
/**
 * A central place to register application converters and formatters. 
 */

public class ApplicationConversionServiceFactoryBean extends FormattingConversionServiceFactoryBean {

	@Override
	protected void installFormatters(FormatterRegistry registry) {
		super.installFormatters(registry);
		// Register application converters and formatters
	}

	public Converter<Blog, String> getBlogToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.diploma.ccms.domain.Blog, java.lang.String>() {
            public String convert(Blog blog) {
                return new StringBuilder().append(blog.getTitle()).append(" ").append(blog.getBody()).append(" ").append(blog.getEnterDate()).toString();
            }
        };
    }

	public Converter<Long, Blog> getIdToBlogConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.diploma.ccms.domain.Blog>() {
            public com.diploma.ccms.domain.Blog convert(java.lang.Long id) {
                return Blog.findBlog(id);
            }
        };
    }

	public Converter<String, Blog> getStringToBlogConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.diploma.ccms.domain.Blog>() {
            public com.diploma.ccms.domain.Blog convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Blog.class);
            }
        };
    }

	public Converter<BlogComment, String> getBlogCommentToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.diploma.ccms.domain.BlogComment, java.lang.String>() {
            public String convert(BlogComment blogComment) {
                return new StringBuilder().append(blogComment.getTitle()).append(" ").append(blogComment.getBody()).append(" ").append(blogComment.getEnterDate()).toString();
            }
        };
    }

	public Converter<Long, BlogComment> getIdToBlogCommentConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.diploma.ccms.domain.BlogComment>() {
            public com.diploma.ccms.domain.BlogComment convert(java.lang.Long id) {
                return BlogComment.findBlogComment(id);
            }
        };
    }

	public Converter<String, BlogComment> getStringToBlogCommentConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.diploma.ccms.domain.BlogComment>() {
            public com.diploma.ccms.domain.BlogComment convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), BlogComment.class);
            }
        };
    }

	public Converter<Calendar, String> getCalendarToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.diploma.ccms.domain.Calendar, java.lang.String>() {
            public String convert(Calendar calendar) {
                return new StringBuilder().append(calendar.getTitle()).toString();
            }
        };
    }

	public Converter<Long, Calendar> getIdToCalendarConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.diploma.ccms.domain.Calendar>() {
            public com.diploma.ccms.domain.Calendar convert(java.lang.Long id) {
                return Calendar.findCalendar(id);
            }
        };
    }

	public Converter<String, Calendar> getStringToCalendarConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.diploma.ccms.domain.Calendar>() {
            public com.diploma.ccms.domain.Calendar convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Calendar.class);
            }
        };
    }

	public Converter<Contact, String> getContactToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.diploma.ccms.domain.Contact, java.lang.String>() {
            public String convert(Contact contact) {
                return new StringBuilder().toString();
            }
        };
    }

	public Converter<Long, Contact> getIdToContactConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.diploma.ccms.domain.Contact>() {
            public com.diploma.ccms.domain.Contact convert(java.lang.Long id) {
                return Contact.findContact(id);
            }
        };
    }

	public Converter<String, Contact> getStringToContactConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.diploma.ccms.domain.Contact>() {
            public com.diploma.ccms.domain.Contact convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Contact.class);
            }
        };
    }

	public Converter<Document, String> getDocumentToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.diploma.ccms.domain.Document, java.lang.String>() {
            public String convert(Document document) {
                return new StringBuilder().append(document.getTitle()).append(" ").append(document.getDescription()).toString();
            }
        };
    }

	public Converter<Long, Document> getIdToDocumentConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.diploma.ccms.domain.Document>() {
            public com.diploma.ccms.domain.Document convert(java.lang.Long id) {
                return Document.findDocument(id);
            }
        };
    }

	public Converter<String, Document> getStringToDocumentConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.diploma.ccms.domain.Document>() {
            public com.diploma.ccms.domain.Document convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Document.class);
            }
        };
    }

	public Converter<DocumentCategory, String> getDocumentCategoryToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.diploma.ccms.domain.DocumentCategory, java.lang.String>() {
            public String convert(DocumentCategory documentCategory) {
                return new StringBuilder().append(documentCategory.getTitle()).append(" ").append(documentCategory.getDescription()).toString();
            }
        };
    }

	public Converter<Long, DocumentCategory> getIdToDocumentCategoryConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.diploma.ccms.domain.DocumentCategory>() {
            public com.diploma.ccms.domain.DocumentCategory convert(java.lang.Long id) {
                return DocumentCategory.findDocumentCategory(id);
            }
        };
    }

	public Converter<String, DocumentCategory> getStringToDocumentCategoryConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.diploma.ccms.domain.DocumentCategory>() {
            public com.diploma.ccms.domain.DocumentCategory convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), DocumentCategory.class);
            }
        };
    }

	public Converter<Message, String> getMessageToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.diploma.ccms.domain.Message, java.lang.String>() {
            public String convert(Message message) {
                return new StringBuilder().append(message.getTitle()).append(" ").append(message.getText()).append(" ").append(message.getDatetime()).toString();
            }
        };
    }

	public Converter<Long, Message> getIdToMessageConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.diploma.ccms.domain.Message>() {
            public com.diploma.ccms.domain.Message convert(java.lang.Long id) {
                return Message.findMessage(id);
            }
        };
    }

	public Converter<String, Message> getStringToMessageConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.diploma.ccms.domain.Message>() {
            public com.diploma.ccms.domain.Message convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Message.class);
            }
        };
    }

	public Converter<Note, String> getNoteToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.diploma.ccms.domain.Note, java.lang.String>() {
            public String convert(Note note) {
                return new StringBuilder().append(note.getTitle()).append(" ").append(note.getText()).append(" ").append(note.getDatetime()).toString();
            }
        };
    }

	public Converter<Long, Note> getIdToNoteConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.diploma.ccms.domain.Note>() {
            public com.diploma.ccms.domain.Note convert(java.lang.Long id) {
                return Note.findNote(id);
            }
        };
    }

	public Converter<String, Note> getStringToNoteConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.diploma.ccms.domain.Note>() {
            public com.diploma.ccms.domain.Note convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Note.class);
            }
        };
    }

	public Converter<Region, String> getRegionToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.diploma.ccms.domain.Region, java.lang.String>() {
            public String convert(Region region) {
                return new StringBuilder().append(region.getRegionName()).toString();
            }
        };
    }

	public Converter<Long, Region> getIdToRegionConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.diploma.ccms.domain.Region>() {
            public com.diploma.ccms.domain.Region convert(java.lang.Long id) {
                return Region.findRegion(id);
            }
        };
    }

	public Converter<String, Region> getStringToRegionConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.diploma.ccms.domain.Region>() {
            public com.diploma.ccms.domain.Region convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Region.class);
            }
        };
    }

	public Converter<Room, String> getRoomToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.diploma.ccms.domain.Room, java.lang.String>() {
            public String convert(Room room) {
                return new StringBuilder().append(room.getRoomName()).toString();
            }
        };
    }

	public Converter<Long, Room> getIdToRoomConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.diploma.ccms.domain.Room>() {
            public com.diploma.ccms.domain.Room convert(java.lang.Long id) {
                return Room.findRoom(id);
            }
        };
    }

	public Converter<String, Room> getStringToRoomConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.diploma.ccms.domain.Room>() {
            public com.diploma.ccms.domain.Room convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Room.class);
            }
        };
    }

	public Converter<RoomCalendar, String> getRoomCalendarToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.diploma.ccms.domain.RoomCalendar, java.lang.String>() {
            public String convert(RoomCalendar roomCalendar) {
                return new StringBuilder().append(roomCalendar.getTitle()).append(" ").append(roomCalendar.getBody()).toString();
            }
        };
    }

	public Converter<Long, RoomCalendar> getIdToRoomCalendarConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.diploma.ccms.domain.RoomCalendar>() {
            public com.diploma.ccms.domain.RoomCalendar convert(java.lang.Long id) {
                return RoomCalendar.findRoomCalendar(id);
            }
        };
    }

	public Converter<String, RoomCalendar> getStringToRoomCalendarConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.diploma.ccms.domain.RoomCalendar>() {
            public com.diploma.ccms.domain.RoomCalendar convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), RoomCalendar.class);
            }
        };
    }

	public Converter<Team, String> getTeamToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.diploma.ccms.domain.Team, java.lang.String>() {
            public String convert(Team team) {
                return new StringBuilder().append(team.getTeamName()).toString();
            }
        };
    }

	public Converter<Long, Team> getIdToTeamConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.diploma.ccms.domain.Team>() {
            public com.diploma.ccms.domain.Team convert(java.lang.Long id) {
                return Team.findTeam(id);
            }
        };
    }

	public Converter<String, Team> getStringToTeamConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.diploma.ccms.domain.Team>() {
            public com.diploma.ccms.domain.Team convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Team.class);
            }
        };
    }

	public Converter<Todo, String> getTodoToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.diploma.ccms.domain.Todo, java.lang.String>() {
            public String convert(Todo todo) {
                return new StringBuilder().append(todo.getTitle()).append(" ").append(todo.getText()).append(" ").append(todo.getEnterDate()).append(" ").append(todo.getDueDate()).toString();
            }
        };
    }

	public Converter<Long, Todo> getIdToTodoConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.diploma.ccms.domain.Todo>() {
            public com.diploma.ccms.domain.Todo convert(java.lang.Long id) {
                return Todo.findTodo(id);
            }
        };
    }

	public Converter<String, Todo> getStringToTodoConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.diploma.ccms.domain.Todo>() {
            public com.diploma.ccms.domain.Todo convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Todo.class);
            }
        };
    }

	public Converter<TodoCategory, String> getTodoCategoryToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.diploma.ccms.domain.TodoCategory, java.lang.String>() {
            public String convert(TodoCategory todoCategory) {
                return new StringBuilder().append(todoCategory.getTitle()).append(" ").append(todoCategory.getDescription()).toString();
            }
        };
    }

	public Converter<Long, TodoCategory> getIdToTodoCategoryConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.diploma.ccms.domain.TodoCategory>() {
            public com.diploma.ccms.domain.TodoCategory convert(java.lang.Long id) {
                return TodoCategory.findTodoCategory(id);
            }
        };
    }

	public Converter<String, TodoCategory> getStringToTodoCategoryConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.diploma.ccms.domain.TodoCategory>() {
            public com.diploma.ccms.domain.TodoCategory convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), TodoCategory.class);
            }
        };
    }

	public Converter<Wiki, String> getWikiToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.diploma.ccms.domain.Wiki, java.lang.String>() {
            public String convert(Wiki wiki) {
                return new StringBuilder().append(wiki.getTitle()).append(" ").append(wiki.getEnterDate()).toString();
            }
        };
    }

	public Converter<Long, Wiki> getIdToWikiConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.diploma.ccms.domain.Wiki>() {
            public com.diploma.ccms.domain.Wiki convert(java.lang.Long id) {
                return Wiki.findWiki(id);
            }
        };
    }

	public Converter<String, Wiki> getStringToWikiConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.diploma.ccms.domain.Wiki>() {
            public com.diploma.ccms.domain.Wiki convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Wiki.class);
            }
        };
    }

	public Converter<WikiCategory, String> getWikiCategoryToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.diploma.ccms.domain.WikiCategory, java.lang.String>() {
            public String convert(WikiCategory wikiCategory) {
                return new StringBuilder().append(wikiCategory.getTitle()).append(" ").append(wikiCategory.getDescription()).toString();
            }
        };
    }

	public Converter<Long, WikiCategory> getIdToWikiCategoryConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.diploma.ccms.domain.WikiCategory>() {
            public com.diploma.ccms.domain.WikiCategory convert(java.lang.Long id) {
                return WikiCategory.findWikiCategory(id);
            }
        };
    }

	public Converter<String, WikiCategory> getStringToWikiCategoryConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.diploma.ccms.domain.WikiCategory>() {
            public com.diploma.ccms.domain.WikiCategory convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), WikiCategory.class);
            }
        };
    }

	public Converter<Worker, String> getWorkerToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.diploma.ccms.domain.Worker, java.lang.String>() {
            public String convert(Worker worker) {
                return new StringBuilder().append(worker.getLogin()).append(" ").append(worker.getPass()).append(" ").append(worker.getName()).append(" ").append(worker.getSurname()).toString();
            }
        };
    }

	public Converter<Long, Worker> getIdToWorkerConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.diploma.ccms.domain.Worker>() {
            public com.diploma.ccms.domain.Worker convert(java.lang.Long id) {
                return Worker.findWorker(id);
            }
        };
    }

	public Converter<String, Worker> getStringToWorkerConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.diploma.ccms.domain.Worker>() {
            public com.diploma.ccms.domain.Worker convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Worker.class);
            }
        };
    }

	public Converter<WorkerJobType, String> getWorkerJobTypeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.diploma.ccms.domain.WorkerJobType, java.lang.String>() {
            public String convert(WorkerJobType workerJobType) {
                return new StringBuilder().append(workerJobType.getJobTypeName()).toString();
            }
        };
    }

	public Converter<Long, WorkerJobType> getIdToWorkerJobTypeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.diploma.ccms.domain.WorkerJobType>() {
            public com.diploma.ccms.domain.WorkerJobType convert(java.lang.Long id) {
                return WorkerJobType.findWorkerJobType(id);
            }
        };
    }

	public Converter<String, WorkerJobType> getStringToWorkerJobTypeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.diploma.ccms.domain.WorkerJobType>() {
            public com.diploma.ccms.domain.WorkerJobType convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), WorkerJobType.class);
            }
        };
    }

	public Converter<WorkerRole, String> getWorkerRoleToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.diploma.ccms.domain.WorkerRole, java.lang.String>() {
            public String convert(WorkerRole workerRole) {
                return new StringBuilder().append(workerRole.getRoleName()).toString();
            }
        };
    }

	public Converter<Long, WorkerRole> getIdToWorkerRoleConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.diploma.ccms.domain.WorkerRole>() {
            public com.diploma.ccms.domain.WorkerRole convert(java.lang.Long id) {
                return WorkerRole.findWorkerRole(id);
            }
        };
    }

	public Converter<String, WorkerRole> getStringToWorkerRoleConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.diploma.ccms.domain.WorkerRole>() {
            public com.diploma.ccms.domain.WorkerRole convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), WorkerRole.class);
            }
        };
    }

	public void installLabelConverters(FormatterRegistry registry) {
        registry.addConverter(getBlogToStringConverter());
        registry.addConverter(getIdToBlogConverter());
        registry.addConverter(getStringToBlogConverter());
        registry.addConverter(getBlogCommentToStringConverter());
        registry.addConverter(getIdToBlogCommentConverter());
        registry.addConverter(getStringToBlogCommentConverter());
        registry.addConverter(getCalendarToStringConverter());
        registry.addConverter(getIdToCalendarConverter());
        registry.addConverter(getStringToCalendarConverter());
        registry.addConverter(getContactToStringConverter());
        registry.addConverter(getIdToContactConverter());
        registry.addConverter(getStringToContactConverter());
        registry.addConverter(getDocumentToStringConverter());
        registry.addConverter(getIdToDocumentConverter());
        registry.addConverter(getStringToDocumentConverter());
        registry.addConverter(getDocumentCategoryToStringConverter());
        registry.addConverter(getIdToDocumentCategoryConverter());
        registry.addConverter(getStringToDocumentCategoryConverter());
        registry.addConverter(getMessageToStringConverter());
        registry.addConverter(getIdToMessageConverter());
        registry.addConverter(getStringToMessageConverter());
        registry.addConverter(getNoteToStringConverter());
        registry.addConverter(getIdToNoteConverter());
        registry.addConverter(getStringToNoteConverter());
        registry.addConverter(getRegionToStringConverter());
        registry.addConverter(getIdToRegionConverter());
        registry.addConverter(getStringToRegionConverter());
        registry.addConverter(getRoomToStringConverter());
        registry.addConverter(getIdToRoomConverter());
        registry.addConverter(getStringToRoomConverter());
        registry.addConverter(getRoomCalendarToStringConverter());
        registry.addConverter(getIdToRoomCalendarConverter());
        registry.addConverter(getStringToRoomCalendarConverter());
        registry.addConverter(getTeamToStringConverter());
        registry.addConverter(getIdToTeamConverter());
        registry.addConverter(getStringToTeamConverter());
        registry.addConverter(getTodoToStringConverter());
        registry.addConverter(getIdToTodoConverter());
        registry.addConverter(getStringToTodoConverter());
        registry.addConverter(getTodoCategoryToStringConverter());
        registry.addConverter(getIdToTodoCategoryConverter());
        registry.addConverter(getStringToTodoCategoryConverter());
        registry.addConverter(getWikiToStringConverter());
        registry.addConverter(getIdToWikiConverter());
        registry.addConverter(getStringToWikiConverter());
        registry.addConverter(getWikiCategoryToStringConverter());
        registry.addConverter(getIdToWikiCategoryConverter());
        registry.addConverter(getStringToWikiCategoryConverter());
        registry.addConverter(getWorkerToStringConverter());
        registry.addConverter(getIdToWorkerConverter());
        registry.addConverter(getStringToWorkerConverter());
        registry.addConverter(getWorkerJobTypeToStringConverter());
        registry.addConverter(getIdToWorkerJobTypeConverter());
        registry.addConverter(getStringToWorkerJobTypeConverter());
        registry.addConverter(getWorkerRoleToStringConverter());
        registry.addConverter(getIdToWorkerRoleConverter());
        registry.addConverter(getStringToWorkerRoleConverter());
    }

	public void afterPropertiesSet() {
        super.afterPropertiesSet();
        installLabelConverters(getObject());
    }
}
