package com.jawa.bookbazaar.googleapi.model;

import java.util.List;

import com.jawa.bookbazaar.googleapi.model.BookVolume.BookVolumeItem;
import com.jawa.bookbazaar.googleapi.model.BookVolume.BookVolumeItem.AccessInfo;
import com.jawa.bookbazaar.googleapi.model.BookVolume.BookVolumeItem.AccessInfo.Epub;
import com.jawa.bookbazaar.googleapi.model.BookVolume.BookVolumeItem.AccessInfo.Pdf;
import com.jawa.bookbazaar.googleapi.model.BookVolume.BookVolumeItem.SaleInfo;
import com.jawa.bookbazaar.googleapi.model.BookVolume.BookVolumeItem.VolumeInfo;

public record BookVolume(String kind, int totalItems, List<BookVolumeItem> items) {
	public static record BookVolumeItem(String kind, String id, String etag, String selfLink, VolumeInfo volumeInfo,
			SaleInfo saleInfo, AccessInfo accessInfo, SearchInfo searchInfo) {
		public static record VolumeInfo(String title, List<String> authors, String publisher, String publishedDate,
				String description, List<IndustryIdentifier> industryIdentifiers, ReadingModes readingModes,
				int pageCount, String printType, List<String> categories, double averageRating, int ratingsCount,
				String maturityRating, boolean allowAnonLogging, String contentVersion,
				PanelizationSummary panelizationSummary, boolean comicsContent, ImageLinks imageLinks, String language,
				String previewLink, String infoLink, String canonicalVolumeLink) {
		}

		public static record SaleInfo(String country, String saleability, boolean isEbook) {
		}

		public static record AccessInfo(String country, String viewability, boolean embeddable, boolean publicDomain,
				String textToSpeechPermission, Epub epub, Pdf pdf, String webReaderLink, String accessViewStatus,
				boolean quoteSharingAllowed) {
			public static record Epub(boolean isAvailable) {
			}

			public static record Pdf(boolean isAvailable) {
			}
		}

		public static record IndustryIdentifier(String type, String identifier) {
		}

		public static record ReadingModes(boolean text, boolean image) {
		}

		public static record PanelizationSummary(boolean containsEpubBubbles, boolean containsImageBubbles) {
		}

		public static record ImageLinks(String smallThumbnail, String thumbnail) {
		}
	}

	public static record SearchInfo(String textSnippet) {
	}
}