package de.cronn.jira.sync.mapping;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import de.cronn.jira.sync.config.JiraSyncConfig;
import de.cronn.jira.sync.domain.JiraIssue;
import de.cronn.jira.sync.domain.JiraPriority;
import de.cronn.jira.sync.service.JiraService;

@Component
public class DefaultPriorityMapper implements PriorityMapper {

	private static final Logger log = LoggerFactory.getLogger(DefaultPriorityMapper.class);

	private JiraSyncConfig syncConfig;

	@Autowired
	public void setSyncConfig(JiraSyncConfig syncConfig) {
		this.syncConfig = syncConfig;
	}

	@Override
	public JiraPriority mapPriority(JiraService jiraTarget, JiraIssue sourceIssue) {
		String sourcePriorityName = getSourcePriorityName(sourceIssue);
		if (sourcePriorityName == null) {
			return null;
		}

		JiraPriority targetPriority = getTargetPriorityName(jiraTarget, syncConfig, sourcePriorityName);
		if (targetPriority == null) {
			return null;
		}

		log.trace("priority: {}  -->  {}", sourcePriorityName, targetPriority);
		return targetPriority;
	}

	private static JiraPriority getTargetPriorityName(JiraService jiraTarget, JiraSyncConfig syncConfig, String sourcePriorityName) {
		String targetPriorityName = syncConfig.getPriorityMapping().get(sourcePriorityName);
		if (targetPriorityName == null) {
			log.warn("no mapping defined for {}", sourcePriorityName);
			return null;
		}

		JiraPriority targetPriority = jiraTarget.getPriorities().stream()
			.filter(priority -> Objects.equals(priority.getName(), targetPriorityName))
			.findFirst()
			.orElse(null);

		if (targetPriority == null) {
			log.warn("target priority '{}' not found", targetPriorityName);
			return null;
		}
		return targetPriority;
	}

	private static String getSourcePriorityName(JiraIssue sourceIssue) {
		JiraPriority sourcePriority = sourceIssue.getFields().getPriority();
		if (sourcePriority == null) {
			return null;
		}

		String sourcePriorityName = sourcePriority.getName();
		Assert.notNull(sourcePriorityName, "sourcePriorityName not set for: " + sourceIssue);
		return sourcePriorityName;
	}
}
