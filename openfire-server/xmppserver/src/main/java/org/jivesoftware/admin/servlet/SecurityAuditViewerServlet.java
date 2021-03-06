package org.jivesoftware.admin.servlet;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

import org.jivesoftware.openfire.security.SecurityAuditEvent;
import org.jivesoftware.openfire.security.SecurityAuditManager;
import org.jivesoftware.openfire.security.SecurityAuditProvider;
import org.jivesoftware.util.ListPager;
import org.jivesoftware.util.ParamUtils;
import org.jivesoftware.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/security-audit-viewer.jsp")
public class SecurityAuditViewerServlet extends HttpServlet {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd");

    private static final String[] SEARCH_FIELDS = {"searchUsername", "searchNode", "searchSummary", "searchFrom", "searchTo"};

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final SecurityAuditProvider securityAuditProvider = SecurityAuditManager.getSecurityAuditProvider();
        final List<SecurityAuditEvent> events;
        if (securityAuditProvider.isWriteOnly()) {
            events = Collections.emptyList();
        } else {
            // The ListPager deals with paging & filtering so fetch all events
            events = securityAuditProvider.getEvents(null, null, Integer.MAX_VALUE, null, null);
        }

        final Search search = new Search(request);
        Predicate<SecurityAuditEvent> predicate = auditEvent -> true;
        if (!search.username.isEmpty()) {
            predicate = predicate.and(event -> StringUtils.containsIgnoringCase(event.getUsername(), search.username));
        }
        if (!search.node.isEmpty()) {
            predicate = predicate.and(event -> StringUtils.containsIgnoringCase(event.getNode(), search.node));
        }
        if (!search.summary.isEmpty()) {
            predicate = predicate.and(event -> StringUtils.containsIgnoringCase(event.getSummary(), search.summary));
        }
        if (!search.from.isEmpty()) {
            try {
                final Date date = DATE_FORMAT.parse(search.from);
                predicate = predicate.and(auditEvent -> !auditEvent.getEventStamp().before(date));
            } catch (final ParseException e) {
                predicate = auditEvent -> false;
            }
        }
        if (!search.to.isEmpty()) {
            try {
                // Intuitively the end date is exclusive, so add an extra day
                final Date date = Date.from(DATE_FORMAT.parse(search.to).toInstant().plus(1, ChronoUnit.DAYS));
                predicate = predicate.and(auditEvent -> auditEvent.getEventStamp().before(date));
            } catch (final ParseException e) {
                predicate = auditEvent -> false;
            }
        }

        final ListPager<SecurityAuditEvent> listPager = new ListPager<>(request, response, events, predicate, SEARCH_FIELDS);

        request.setAttribute("securityAuditProvider", securityAuditProvider);
        request.setAttribute("listPager", listPager);
        request.setAttribute("search", search);
        request.getRequestDispatcher("security-audit-viewer-jsp.jsp").forward(request, response);
    }

    public static class Search {
        private final String username;
        private final String node;
        private final String summary;
        private final String from;
        private final String to;

        public Search(final HttpServletRequest request) {
            this.username = ParamUtils.getStringParameter(request, "searchUsername", "").trim();
            this.node = ParamUtils.getStringParameter(request, "searchNode", "").trim();
            this.summary = ParamUtils.getStringParameter(request, "searchSummary", "").trim();
            this.from = ParamUtils.getStringParameter(request, "searchFrom", "").trim();
            this.to = ParamUtils.getStringParameter(request, "searchTo", "").trim();
        }

        public String getUsername() {
            return username;
        }

        public String getNode() {
            return node;
        }

        public String getSummary() {
            return summary;
        }

        public String getFrom() {
            return from;
        }

        public String getTo() {
            return to;
        }
    }
}
