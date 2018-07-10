package systems.whitestar.mediasite_monitor.Models;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/**
 * @author Tom Paulus
 * Created on 5/7/18.
 */
@Data
public class Schedule {
    @SerializedName("odata.id")
    private String odataId;

    @SerializedName("Presenters@odata.navigationLinkUrl")
    private String presentersOdataNavigationLinkUrl;

    @SerializedName("Recurrences@odata.navigationLinkUrl")
    private String recurrencesOdataNavigationLinkUrl;

    @SerializedName("Tags@odata.navigationLinkUrl")
    private String tagsOdataNavigationLinkUrl;

    @SerializedName("Recorder@odata.navigationLinkUrl")
    private String recorderOdataNavigationLinkUrl;

    @SerializedName("Player@odata.navigationLinkUrl")
    private String playerOdataNavigationLinkUrl;

    @SerializedName("Folder@odata.navigationLinkUrl")
    private String folderOdataNavigationLinkUrl;

    @SerializedName("SlideContent@odata.navigationLinkUrl")
    private String slideContentOdataNavigationLinkUrl;

    @SerializedName("OnDemandContent@odata.navigationLinkUrl")
    private String onDemandContentOdataNavigationLinkUrl;

    @SerializedName("BroadcastContent@odata.navigationLinkUrl")
    private String broadcastContentOdataNavigationLinkUrl;

    @SerializedName("PodcastContent@odata.navigationLinkUrl")
    private String podcastContentOdataNavigationLinkUrl;

    @SerializedName("PublishToGoContent@odata.navigationLinkUrl")
    private String publishToGoContentOdataNavigationLinkUrl;

    @SerializedName("OcrContent@odata.navigationLinkUrl")
    private String ocrContentOdataNavigationLinkUrl;

    @SerializedName("CaptionContent@odata.navigationLinkUrl")
    private String captionContentOdataNavigationLinkUrl;

    @SerializedName("VideoPodcastContent@odata.navigationLinkUrl")
    private String videoPodcastContentOdataNavigationLinkUrl;

    @SerializedName("ExternalPublishingContent@odata.navigationLinkUrl")
    private String externalPublishingContentOdataNavigationLinkUrl;

    @SerializedName("Modules@odata.navigationLinkUrl")
    private String modulesOdataNavigationLinkUrl;

    @SerializedName("Id")
    private String id;

    @SerializedName("Name")
    private String name;

    @SerializedName("TitleType")
    private String titleType;

    @SerializedName("FolderId")
    private String folderId;

    @SerializedName("ScheduleTemplateId")
    private Object scheduleTemplateId;

    @SerializedName("IsLive")
    private Boolean isLive;

    @SerializedName("IsUploadAutomatic")
    private Boolean isUploadAutomatic;

    @SerializedName("RecorderWebServiceUrl")
    private String recorderWebServiceUrl;

    @SerializedName("RecorderEncryptionKey")
    private Object recorderEncryptionKey;

    @SerializedName("DeviceId")
    private String deviceId;

    @SerializedName("RecorderId")
    private String recorderId;

    @SerializedName("RecorderName")
    private String recorderName;

    @SerializedName("RecorderUsername")
    private String recorderUsername;

    @SerializedName("RecorderPassword")
    private String recorderPassword;

    @SerializedName("AdvanceCreationTime")
    private Integer advanceCreationTime;

    @SerializedName("AdvanceLoadTimeInSeconds")
    private Integer advanceLoadTimeInSeconds;

    @SerializedName("ReceipientsEmailAddresses")
    private String receipientsEmailAddresses;

    @SerializedName("CreatePresentation")
    private Boolean createPresentation;

    @SerializedName("LoadPresentation")
    private Boolean loadPresentation;

    @SerializedName("AutoStart")
    private Boolean autoStart;

    @SerializedName("AutoStop")
    private Boolean autoStop;

    @SerializedName("SendersEmail")
    private String sendersEmail;

    @SerializedName("CDNPublishingPoint")
    private Object cDNPublishingPoint;

    @SerializedName("NextNumberInSchedule")
    private Integer nextNumberInSchedule;

    @SerializedName("NotifyPresenter")
    private Boolean notifyPresenter;

    @SerializedName("TimeZoneRegistryKey")
    private String timeZoneRegistryKey;

    @SerializedName("LastModified")
    private String lastModified;

    @SerializedName("DeleteInactive")
    private Boolean deleteInactive;

    @SerializedName("Description")
    private String description;

    @SerializedName("IsForumEnabled")
    private Boolean isForumEnabled;

    @SerializedName("IsOnDemand")
    private Boolean isOnDemand;

    @SerializedName("IsPollsEnabled")
    private Boolean isPollsEnabled;

    @SerializedName("ReviewEditApproveEnabled")
    private Boolean reviewEditApproveEnabled;

    @SerializedName("ReplaceAclWithPolicy")
    private Boolean replaceAclWithPolicy;

    @SerializedName("UseAdaptiveCapture")
    private Boolean useAdaptiveCapture;

    @SerializedName("PlayerId")
    private String playerId;

    @SerializedName("Policy")
    private List<Object> policy = null;

    @SerializedName("CustomFieldValues")
    private List<Object> customFieldValues = null;


    @Data
    public static class Recurrence {
        @SerializedName("odata.id")
        private String odataId;

        @SerializedName("Id")
        private Integer id;

        @SerializedName("MediasiteId")
        private String mediasiteId;

        @SerializedName("RecordDuration")
        private Integer recordDuration;

        @SerializedName("StartRecordDateTime")
        private String startRecordDateTime;

        @SerializedName("EndRecordDateTime")
        private String endRecordDateTime;

        @SerializedName("RecurrencePattern")
        private String recurrencePattern;

        @SerializedName("NextScheduleTime")
        private String nextScheduleTime;

        @SerializedName("RecurrencePatternType")
        private Integer recurrencePatternType;

        @SerializedName("RecurrenceFrequency")
        private Integer recurrenceFrequency;

        @SerializedName("WeekDayOnly")
        private Boolean weekDayOnly;

        @SerializedName("DaysOfTheWeek")
        private String daysOfTheWeek;

        @SerializedName("WeekOfTheMonth")
        private String weekOfTheMonth;

        @SerializedName("DayOfTheMonth")
        private Integer dayOfTheMonth;

        @SerializedName("MonthOfTheYear")
        private String monthOfTheYear;

        @SerializedName("ExcludeHolidays")
        private Boolean excludeHolidays;

        @SerializedName("ExcludeDateRangeList")
        private List<Object> excludeDateRangeList = null;

        private Schedule parentSchedule;
    }
}
